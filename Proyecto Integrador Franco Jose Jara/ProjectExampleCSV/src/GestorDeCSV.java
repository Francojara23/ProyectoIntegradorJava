import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GestorDeCSV {
    private String rutaArchivo;

    public GestorDeCSV(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void escribirCSV(ArrayList<Producto> nuevosProductos) {
        try {
            ArrayList<Producto> productosActuales = leerCSV();

            File archivo = new File(rutaArchivo);
            boolean archivoExiste = archivo.exists();
            boolean productoExiste = false;
            
            FileWriter writer = new FileWriter(archivo, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            if (!archivoExiste) {
            	bufferedWriter.write("#ID;NOMBRE;DESCRIPCION;PRECIO;FECHAALTA");
            	bufferedWriter.newLine();
            }
            for (Producto nuevoProducto : nuevosProductos) {                
            	if (!productosActuales.contains(nuevoProducto)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaAltaStr = dateFormat.format(nuevoProducto.getFechaAlta());
                    bufferedWriter.write(
                            nuevoProducto.getId() + ";" +
                                    nuevoProducto.getNombre() + ";" +
                                    nuevoProducto.getDescripcion() + ";" +
                                    nuevoProducto.getPrecio() + ";" +
                                    fechaAltaStr);
                    bufferedWriter.newLine();
                    productosActuales.add(nuevoProducto);
                    productoExiste = false;
                    //System.out.println("Registro agregado correctamente.");
                } else {
                    productoExiste = true;
                }
            }
            if (productoExiste == true) {
            	System.out.println("El ID de Registro ya existe. Cargar otro ID.");
			}else {
				System.out.println("Registro agregado correctamente.");
			}
            
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Producto> leerCSV() {
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;

            // Saltamos la cabecera
            bufferedReader.readLine();
            while ((linea = bufferedReader.readLine()) != null) {
                String[] campos = linea.split(";");
                Integer id = Integer.parseInt(campos[0]);
                String nombre = campos[1];
                String descripcion = campos[2];
                Double precio = Double.parseDouble(campos[3]);
                Date fechaAlta = new SimpleDateFormat("dd/MM/yyyy").parse(campos[4]);

                Producto producto = new Producto(id, nombre, descripcion, precio, fechaAlta);
                productos.add(producto);
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no existe.");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return productos;
    }
}