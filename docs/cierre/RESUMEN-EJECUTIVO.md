# Resumen ejecutivo — Ride Hailing

Pedimos aprobar la continuidad de la arquitectura híbrida y priorizar pruebas de
integración y observabilidad antes de añadir más servicios.

Los pasajeros necesitan solicitar viajes y consultar su estado; los conductores
necesitan recibir ofertas y mantener su disponibilidad. El sistema también debe
calcular tarifas y guardar las calificaciones sin perder información cuando un
componente temporalmente no responde.

Proponemos mantener una aplicación modular para las capacidades que todavía
pueden evolucionar juntas y servicios independientes donde hay una necesidad
clara de separar datos o trabajo. Una entrada común recibe las solicitudes y
las dirige al módulo apropiado. Cada servicio conserva su información y se
comunica con los demás mediante acuerdos explícitos.

**Qué ganamos / qué cuesta**

- **Ganamos:** podemos cambiar algunas capacidades con menor impacto en las
  demás y mantener la información de cada una bajo su responsabilidad.
- **Cuesta:** más componentes, bases de datos y acuerdos que probar y operar que
  en una sola aplicación.

**Principales riesgos y cómo los controlamos**

1. Si el servicio de viajes no responde, no se debe aceptar una calificación que
   no se pudo validar. Se informa el error y se limita la repetición de llamadas.
2. Si el sistema de mensajería se detiene, los cambios de viaje deben conservar
   sus avisos pendientes para publicarlos después; hacen falta seguimiento y
   pruebas de recuperación.

**Siguiente paso:** el equipo debe validar los escenarios y la matriz, ejecutar
el flujo completo por la entrada común y acordar qué métricas y trazas necesita
para diagnosticar fallos antes de ampliar la arquitectura.

**Prueba de lectura:** pendiente con una persona ajena al curso; se debe
confirmar que identifica qué se pide, por qué y cuál es el riesgo principal.
