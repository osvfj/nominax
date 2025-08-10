package com.nominax.service;

import com.nominax.dao.*;
import com.nominax.entity.*;
import com.nominax.dao.impl.*;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author osvfj
 */

public class GeneradorNominas {
    private static final double TASA_ARS = 0.0304;
    private static final double TASA_AFP = 0.0287;
    
    private final EmpleadoDAO empleadoDAO;
    private final CooperativaDAO cooperativaDAO;
    private final NominaDAO nominaDAO;
    private final GeneradorPDFNomina generadorPDF;

    public GeneradorNominas() {
        this.empleadoDAO = new EmpleadoDAOFile();
        this.cooperativaDAO = new CooperativaDAOFile();
        this.nominaDAO = new NominaDAOFile();
        this.generadorPDF = new GeneradorPDFNomina();
    }

    public void generarNominas(int mes, int anio) throws IOException, IllegalStateException {
        if (nominaDAO.existeNominaMesAnio(mes, anio)) {
            throw new IllegalStateException(
                "Ya existe una nómina generada para " + mes + "/" + anio
            );
        }

        List<Empleado> empleados = empleadoDAO.obtenerTodos();
        
        if (empleados.isEmpty()) {
            throw new IllegalStateException("No hay empleados registrados");
        }

        // Crear fecha de nómina (día 30)
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes - 1, 30); 
        Date fechaNomina = cal.getTime();

        List<Nomina> nominas = new ArrayList<>();

        for (Empleado empleado : empleados) {
            Nomina nomina = calcularNomina(empleado, fechaNomina);
            nominas.add(nomina);
            
            // Actualizar balance de cooperativa si aplica
            if (empleado.getCoop() && nomina.getDescuentoCooperativa() > 0) {
                actualizarBalanceCooperativa(empleado.getId(), nomina.getDescuentoCooperativa());
            }
        }

        // Guardar todas las nóminas
        for (Nomina nomina : nominas) {
            nominaDAO.guardar(nomina);
        }

        generadorPDF.generarPDFsNominas(nominas, empleados, mes, anio);
    }

    private Nomina calcularNomina(Empleado empleado, Date fecha) throws IOException {
        double salarioBase = empleado.getSalario();
        
        double descuentoARS = salarioBase * TASA_ARS;
        double descuentoAFP = salarioBase * TASA_AFP;
        double descuentoCooperativa = calcularDescuentoCooperativa(empleado, salarioBase);
        
        double baseGravable = salarioBase - descuentoARS - descuentoAFP;
        double descuentoISR = calcularISR(baseGravable);
        
        double salarioNeto = salarioBase - descuentoARS - descuentoAFP 
                           - descuentoCooperativa - descuentoISR;

        return new Nomina(
            empleado.getId(),
            fecha,
            salarioBase,
            descuentoARS,
            descuentoAFP,
            descuentoCooperativa,
            descuentoISR,
            salarioNeto,
            true
        );
    }

  
   private double calcularDescuentoCooperativa(Empleado empleado, double salarioBase) throws IOException {
       if (!empleado.getCoop()) {
           return 0.0;
       }

       Optional<Cooperativa> cooperativaOpt = cooperativaDAO.buscarPorIdEmpleado(
           Integer.parseInt(empleado.getId())
       );

       if (cooperativaOpt.isPresent()) {
           Cooperativa cooperativa = cooperativaOpt.get();
           double porcentajeDescuento = cooperativa.getDescuento(); // Por ejemplo: 4.5 para 4.5%

           // Convertir el porcentaje a decimal y calcular el monto del descuento
           double montoDescuento = salarioBase * (porcentajeDescuento / 100.0);

           return montoDescuento;
       }

       return 0.0;
   }

    private double calcularISR(double baseGravable) {
        if (baseGravable <= 34685.00) {
            return 0.0;
        } else if (baseGravable <= 52027.42) {
            return (baseGravable - 34685.00) * 0.15;
        } else if (baseGravable <= 72260.25) {
            return 2601.33 + (baseGravable - 52027.42) * 0.20;
        } else {
            return 6648.00 + (baseGravable - 72260.25) * 0.25;
        }
    }

    private void actualizarBalanceCooperativa(String idEmpleado, double montoDescuento) throws IOException {
        Optional<Cooperativa> cooperativaOpt = cooperativaDAO.buscarPorIdEmpleado(
            Integer.parseInt(idEmpleado)
        );
        
        if (cooperativaOpt.isPresent()) {
            Cooperativa cooperativa = cooperativaOpt.get();
            // Sumar el monto del descuento al balance actual
            cooperativa.setBalance(cooperativa.getBalance() + montoDescuento);
            cooperativaDAO.actualizar(cooperativa);
        }
    }
}