# TEST API CARGADOR CNMC

Casos de prueba para probar API Carga de la Sede de la CNMC

[https://api.cnmc.gob.es/doc](https://api.cnmc.gob.es/doc)

Se utilizan java beans transformados en mensajes JSON para la utilización de los servicios REST.

La securización se realiza con OAuth y la librería [Scribe](https://github.com/scribejava/scribejava)

# Ejemplo de fichero de configuración

    baseUrl=https://apipre.cnmc.gob.es
    consumerKey=identificador oauth
    consumerSecret=credenciales oauth
    idProcedimiento=1
    nifEmpresa=CIF empresa
    nifPresentador=NIF contacto de la empresa
    tipoFichero=SGDA
    estado=TODAS
    fichero1=ruta fichero de pruebas
    fichero2=ruta fichero de pruebas
    fichero3=ruta fichero de pruebas
