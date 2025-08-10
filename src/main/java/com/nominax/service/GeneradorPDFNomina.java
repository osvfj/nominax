package com.nominax.service;

import com.nominax.dao.CooperativaDAO;
import com.nominax.entity.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.nominax.dao.impl.CooperativaDAOFile;
import com.nominax.util.ExploradorArchivos;
import com.nominax.util.ManejadorArchivos;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class GeneradorPDFNomina {
    private static final DecimalFormat dineroFormato = new DecimalFormat("#,##0.00");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    private final CooperativaDAO cooperativaDAO;

    public GeneradorPDFNomina() {
        this.cooperativaDAO = new CooperativaDAOFile();
    }

    public void generarPDFsNominas(List<Nomina> nominas, List<Empleado> empleados, 
                                  int mes, int anio) throws IOException {
        String rutaDirectorioNominas = String.format("nominas%s%d%s%02d", 
            File.separator, anio, File.separator, mes);
        ManejadorArchivos.crearDirectorioSiNoExiste(rutaDirectorioNominas);

        for (Nomina nomina : nominas) {
            Empleado empleado = empleados.stream()
                .filter(e -> e.getId().equals(nomina.getIdEmpleado()))
                .findFirst()
                .orElse(null);
            
            if (empleado != null) {
                generarPDFNomina(nomina, empleado, mes, anio, rutaDirectorioNominas);
            }
        }
        
        ExploradorArchivos.abrirExploradorEnRuta(ManejadorArchivos.obtenerRutaBase() + 
                                File.separator + rutaDirectorioNominas);
    }

    private void generarPDFNomina(Nomina nomina, Empleado empleado, int mes, int anio, 
                                 String rutaDirectorio) throws IOException {
        try {
            // Construir la ruta completa del archivo
            String nombreArchivo = String.format("%s_%s_%s_%s.pdf", nomina.getIdEmpleado(), empleado.getNombre(), empleado.getApellidoPat(), empleado.getApellidoMat());
            String rutaCompleta = ManejadorArchivos.obtenerRutaBase() + 
                                File.separator + rutaDirectorio + 
                                File.separator + nombreArchivo;
            
            Document document = new Document(PageSize.LETTER);
            PdfWriter.getInstance(document, new FileOutputStream(rutaCompleta));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

            // Encabezado principal
            Paragraph title = new Paragraph("SISTEMA DE NÓMINAS - NOMINAX", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            Paragraph subtitle = new Paragraph("VOLANTE DE PAGO", headerFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(new Paragraph(" "));

            Paragraph periodo = new Paragraph(
                String.format("PERÍODO: %02d/%d", mes, anio), headerFont
            );
            periodo.setAlignment(Element.ALIGN_CENTER);
            document.add(periodo);
            document.add(new Paragraph(" "));

            // Información del empleado
            document.add(new Paragraph("INFORMACIÓN DEL EMPLEADO", headerFont));
            document.add(new Paragraph(" ", normalFont));
            
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setWidths(new float[]{1, 2});

            // Datos del empleado
            addInfoRow(infoTable, "ID Empleado:", empleado.getId(), boldFont, normalFont);
            addInfoRow(infoTable, "Nombre Completo:", 
                empleado.getNombre() + " " + empleado.getApellidoPat() + " " + empleado.getApellidoMat(), 
                boldFont, normalFont);
            addInfoRow(infoTable, "Dirección:", empleado.getDireccion(), boldFont, normalFont);
            addInfoRow(infoTable, "Teléfono:", empleado.getTelefono().toString(), boldFont, normalFont);
            addInfoRow(infoTable, "Fecha de Ingreso:", dateFormat.format(empleado.getFechaIngreso()), boldFont, normalFont);
            addInfoRow(infoTable, "Fecha de Generación:", dateFormat.format(nomina.getFecha()), boldFont, normalFont);
            
            document.add(infoTable);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("DETALLE DE NÓMINA", headerFont));
            document.add(new Paragraph(" ", normalFont));

            PdfPTable nominaTable = new PdfPTable(3);
            nominaTable.setWidthPercentage(100);
            nominaTable.setWidths(new float[]{3, 1, 1.5f});

            // Headers de la tabla
            PdfPCell headerConcepto = new PdfPCell(new Phrase("CONCEPTO", headerFont));
            PdfPCell headerTasa = new PdfPCell(new Phrase("TASA/PORCENTAJE", headerFont));
            PdfPCell headerMonto = new PdfPCell(new Phrase("MONTO (RD$)", headerFont));
            
            headerConcepto.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTasa.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerMonto.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            headerConcepto.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerTasa.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerMonto.setBackgroundColor(BaseColor.LIGHT_GRAY);
            
            nominaTable.addCell(headerConcepto);
            nominaTable.addCell(headerTasa);
            nominaTable.addCell(headerMonto);

            // Salario base
            addNominaRow(nominaTable, "Salario Base", "-", 
                "$" + dineroFormato.format(nomina.getSalarioBase()), 
                normalFont, BaseColor.WHITE);

            // Separador
            addSeparatorRow(nominaTable, "DESCUENTOS", headerFont);

            // Descuento ARS
            addNominaRow(nominaTable, "Seguro de Salud (ARS)", "3.04%", 
                "-$" + dineroFormato.format(nomina.getDescuentoARS()), 
                normalFont, BaseColor.WHITE);

            // Descuento AFP
            addNominaRow(nominaTable, "Administradora de Pensiones (AFP)", "2.87%", 
                "-$" + dineroFormato.format(nomina.getDescuentoAFP()), 
                normalFont, BaseColor.WHITE);

            // Descuento Cooperativa (si aplica)
            if (nomina.getDescuentoCooperativa() > 0) {
                String porcentajeCooperativa = obtenerPorcentajeCooperativa(empleado);
                addNominaRow(nominaTable, "Cooperativa", porcentajeCooperativa, 
                    "-$" + dineroFormato.format(nomina.getDescuentoCooperativa()), 
                    normalFont, BaseColor.WHITE);
            }

            // ISR
            addNominaRow(nominaTable, "Impuesto Sobre la Renta (ISR)", "Variable", 
                "-$" + dineroFormato.format(nomina.getDescuentoISR()), 
                normalFont, BaseColor.WHITE);

            // Separador para total
            addSeparatorRow(nominaTable, "", normalFont);

            // Salario neto
            addNominaRow(nominaTable, "SALARIO NETO", "", 
                "$" + dineroFormato.format(nomina.getSalarioNeto()), 
                headerFont, new BaseColor(230, 255, 230));

            document.add(nominaTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            double totalDescuentos = nomina.getDescuentoARS() + nomina.getDescuentoAFP() + 
                                   nomina.getDescuentoCooperativa() + nomina.getDescuentoISR();

            PdfPTable resumenTable = new PdfPTable(2);
            resumenTable.setWidthPercentage(60);
            resumenTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addInfoRow(resumenTable, "Total Ingresos:", 
                "$" + dineroFormato.format(nomina.getSalarioBase()), boldFont, normalFont);
            addInfoRow(resumenTable, "Total Descuentos:", 
                "$" + dineroFormato.format(totalDescuentos), boldFont, normalFont);
            addInfoRow(resumenTable, "Neto a Cobrar:", 
                "$" + dineroFormato.format(nomina.getSalarioNeto()), headerFont, headerFont);

            document.add(resumenTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Paragraph footer1 = new Paragraph(
                "Este documento es generado automáticamente por el sistema NOMINAX", 
                new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)
            );
            footer1.setAlignment(Element.ALIGN_CENTER);
            document.add(footer1);
            
            Paragraph footer2 = new Paragraph(
                String.format("Archivo: %s", nombreArchivo), 
                new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC)
            );
            footer2.setAlignment(Element.ALIGN_CENTER);
            document.add(footer2);

            document.close();

            System.out.println("PDF generado: " + rutaCompleta);

        } catch (Exception e) {
            throw new IOException("Error generando PDF para empleado " + 
                nomina.getIdEmpleado() + ": " + e.getMessage(), e);
        }
    }

    private void addInfoRow(PdfPTable table, String label, String value, 
                           Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        
        labelCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPaddingBottom(5);
        valueCell.setPaddingBottom(5);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void addNominaRow(PdfPTable table, String concepto, String tasa, 
                             String monto, Font font, BaseColor backgroundColor) {
        PdfPCell conceptoCell = new PdfPCell(new Phrase(concepto, font));
        PdfPCell tasaCell = new PdfPCell(new Phrase(tasa, font));
        PdfPCell montoCell = new PdfPCell(new Phrase(monto, font));

        conceptoCell.setBackgroundColor(backgroundColor);
        tasaCell.setBackgroundColor(backgroundColor);
        montoCell.setBackgroundColor(backgroundColor);

        tasaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        montoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

        conceptoCell.setPadding(8);
        tasaCell.setPadding(8);
        montoCell.setPadding(8);

        table.addCell(conceptoCell);
        table.addCell(tasaCell);
        table.addCell(montoCell);
    }

    private void addSeparatorRow(PdfPTable table, String texto, Font font) {
        PdfPCell separatorCell = new PdfPCell(new Phrase(texto, font));
        separatorCell.setColspan(3);
        separatorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        separatorCell.setBackgroundColor(new BaseColor(240, 240, 240));
        separatorCell.setPadding(5);
        table.addCell(separatorCell);
    }

    private String obtenerPorcentajeCooperativa(Empleado empleado) {
        try {
            Optional<Cooperativa> cooperativaOpt = cooperativaDAO.buscarPorIdEmpleado(
                Integer.parseInt(empleado.getId())
            );
            
            if (cooperativaOpt.isPresent()) {
                double porcentaje = cooperativaOpt.get().getDescuento();
                return String.format("%.1f%%", porcentaje);
            }
        } catch (Exception e) {
            // Si hay error, devolver texto genérico
        }
        return "Variable";
    }
}