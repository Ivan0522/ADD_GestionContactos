import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionContactos {

    private static final String ARCHIVO_XML_PATH = "contactos.xml";
    private static final String ARCHIVO_CSV_PATH = "contactos.csv";

    // Método para agregar un nuevo contacto al fichero XML
    public void agregarContacto(Contacto contacto) {
        try {
            File file = new File(ARCHIVO_XML_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            // Si el archivo ya existe, cargar el documento existente
            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                // Si no existe, crear un nuevo documento XML
                doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("contactos");
                doc.appendChild(rootElement);
            }

            // Crear un nuevo elemento "contacto" y agregar sus elementos hijos
            Element nuevoContacto = doc.createElement("contacto");
            // Elemento nombre
            Element nombreElemento = doc.createElement("nombre");
            nombreElemento.appendChild(doc.createTextNode(contacto.getNombre()));
            nuevoContacto.appendChild(nombreElemento);
            // Elemento telefono
            Element telefonoElemento = doc.createElement("telefono");
            telefonoElemento.appendChild(doc.createTextNode(contacto.getTelefono()));
            nuevoContacto.appendChild(telefonoElemento);
            // Elemento direccion
            Element direccionElemento = doc.createElement("direccion");
            direccionElemento.appendChild(doc.createTextNode(contacto.getDireccion()));
            nuevoContacto.appendChild(direccionElemento);

            // Añadir el nuevo contacto al elemento raíz
            doc.getDocumentElement().appendChild(nuevoContacto);

            // Guardar los cambios en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("----- Contacto agregado exitosamente -----");

        } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
            System.out.println("ParserConfigurationException > Error en la configuración del parser XML.\n" +
                    "IOException > Error de entrada/salida al manejar el archivo XML.\n" +
                    "TransformerException > Error al transformar el documento XML.\n" +
                    "SAXException > Error al analizar el archivo XML.\n" +
                    e.getMessage());
        }
    }

    // Método para buscar un contacto por nombre
    public void buscarContacto(String nombreBuscado) {
        try {
            File file = new File(ARCHIVO_XML_PATH);
            if (!file.exists()) {
                System.out.println("***** No se encontraron contactos. El archivo no existe *****");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            // Obtiene todos los elementos 'contacto' del documento XML y los almacena en un NodeList para poder iterar sobre ellos
            NodeList contactos = doc.getElementsByTagName("contacto");
            // Lista vacía para almacenar los elementos 'contacto' que coincidan con el criterio de búsqueda
            List<Element> contactosEncontrados = new ArrayList<>();

            for (int i = 0; i < contactos.getLength(); i++) {
                Element contacto = (Element) contactos.item(i);
                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();

                if (nombre.equalsIgnoreCase(nombreBuscado)) {
                    contactosEncontrados.add(contacto);
                }
            }

            if (!contactosEncontrados.isEmpty()) {
                int contadorContacto = 1;
                System.out.println("Contactos encontrados:");
                // Foreach para extracer cada contacto
                for (Element contacto : contactosEncontrados) {
                    String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
                    String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
                    String direccion = contacto.getElementsByTagName("direccion").item(0).getTextContent();
                    System.out.println(contadorContacto + ". Nombre: " + nombre + ", Teléfono: " + telefono + ", Dirección: " + direccion);
                    System.out.println("----------------------------------------------------");
                    contadorContacto ++;
                }
            } else {
                System.out.println("***** Contacto no encontrado *****");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para modificar un contacto por nombre
    public void modificarContacto(String nombreBuscado, Contacto nuevosDatos) {
        try {
            File file = new File(ARCHIVO_XML_PATH);
            if (!file.exists()) {
                System.out.println("***** No se encontraron contactos. El archivo no existe *****");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList contactos = doc.getElementsByTagName("contacto");
            List<Element> contactosEncontrados = new ArrayList<>();

            for (int i = 0; i < contactos.getLength(); i++) {
                Element contacto = (Element) contactos.item(i);
                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();

                if (nombre.equalsIgnoreCase(nombreBuscado)) {
                    contactosEncontrados.add(contacto);
                }
            }

            if (!contactosEncontrados.isEmpty()) {
                System.out.println("Contactos encontrados para modificar:");
                for (int i = 0; i < contactosEncontrados.size(); i++) {
                    Element contacto = contactosEncontrados.get(i);
                    String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
                    String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
                    String direccion = contacto.getElementsByTagName("direccion").item(0).getTextContent();
                    System.out.println((i + 1) + ". Nombre: " + nombre + ", Teléfono: " + telefono + ", Dirección: " + direccion);
                }
                System.out.print(">> Seleccione el número del contacto a modificar: ");
                Scanner scanner = new Scanner(System.in);
                int seleccion = scanner.nextInt() - 1;
                scanner.nextLine(); // Consumir línea

                if (seleccion > 0 && seleccion < contactosEncontrados.size()) {
                    Element contacto = contactosEncontrados.get(seleccion);
                    contacto.getElementsByTagName("nombre").item(0).setTextContent(nuevosDatos.getNombre());
                    contacto.getElementsByTagName("telefono").item(0).setTextContent(nuevosDatos.getTelefono());
                    contacto.getElementsByTagName("direccion").item(0).setTextContent(nuevosDatos.getDireccion());

                    // Guardar los cambios en el archivo XML
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(file);
                    transformer.transform(source, result);
                    System.out.println("----- Contacto modificado exitosamente -----");
                } else {
                    System.out.println("***** Selección no válida *****");
                }
            } else {
                System.out.println("***** Contacto no encontrado *****");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un contacto por nombre
    public void eliminarContacto(String nombreBuscado) {
        try {
            File file = new File(ARCHIVO_XML_PATH);
            if (!file.exists()) {
                System.out.println("***** No se encontraron contactos. El archivo no existe *****");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList contactos = doc.getElementsByTagName("contacto");
            List<Element> contactosEncontrados = new ArrayList<>();

            for (int i = 0; i < contactos.getLength(); i++) {
                Element contacto = (Element) contactos.item(i);
                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();

                if (nombre.equalsIgnoreCase(nombreBuscado)) {
                    contactosEncontrados.add(contacto);
                }
            }

            if (!contactosEncontrados.isEmpty()) {
                System.out.println("Contactos encontrados para eliminar:");
                for (int i = 0; i < contactosEncontrados.size(); i++) {
                    Element contacto = contactosEncontrados.get(i);
                    String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
                    String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
                    String direccion = contacto.getElementsByTagName("direccion").item(0).getTextContent();
                    System.out.println((i + 1) + ". Nombre: " + nombre + ", Teléfono: " + telefono + ", Dirección: " + direccion);
                }
                System.out.print(">> Seleccione el número del contacto a eliminar: ");
                Scanner scanner = new Scanner(System.in);
                int seleccion = scanner.nextInt() - 1;
                scanner.nextLine(); // Consumir línea

                if (seleccion > 0 && seleccion < contactosEncontrados.size()) {
                    Element contacto = contactosEncontrados.get(seleccion);
                    contacto.getParentNode().removeChild(contacto);

                    // Guardar los cambios en el archivo XML
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(file);
                    transformer.transform(source, result);
                    System.out.println("----- Contacto eliminado exitosamente -----");
                } else {
                    System.out.println("***** Selección no válida *****");
                }
            } else {
                System.out.println("***** Contacto no encontrado *****");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para exportar contactos a CSV
    public void exportarContactosACSV() {
        try {
            File file = new File(ARCHIVO_XML_PATH);
            if (!file.exists()) {
                System.out.println("***** No se encontraron contactos. El archivo no existe *****");
                return;
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList contactos = doc.getElementsByTagName("contacto");

            FileWriter csvWriter = new FileWriter(ARCHIVO_CSV_PATH);
            csvWriter.append("Nombre,Teléfono,Dirección\n");

            for (int i = 0; i < contactos.getLength(); i++) {
                Element contacto = (Element) contactos.item(i);
                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
                String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
                String direccion = contacto.getElementsByTagName("direccion").item(0).getTextContent();
                csvWriter.append(nombre).append(",").append(telefono).append(",").append(direccion).append("\n");
            }

            // flush() fuerza que este contenido en el búfer se vacíe hacia el archivo.
            csvWriter.flush();
            csvWriter.close();
            System.out.println("----- Contactos exportados exitosamente a " + ARCHIVO_CSV_PATH + " -----");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}