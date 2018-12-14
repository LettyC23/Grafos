
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class Arista implements Comparable<Arista>
{
    private Vertice vertice1, vertice2;
    private int peso;

  
    public Arista(Vertice vertice1, Vertice vertice2)
    {
	this(vertice1, vertice2, 1);
    }

   
    public Arista(Vertice vertice1, Vertice vertice2, int peso)
    {
	if(vertice1.getEtiqueta().compareTo(vertice2.getEtiqueta()) <= 0)
	    {
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
	    }
	else
	    {
		this.vertice1 = vertice2;
		this.vertice2 = vertice1;
	    }

     	this.peso = peso;
		
    }


    public Vertice getVecinoDe(Vertice actual)
    {
	if (actual.equals(this.vertice1))
	    return this.vertice2;
	else if( actual.equals(this.vertice2))
	    return this.vertice1;
	else
	    return null;
    }

    
    public Vertice getVertice1()
    {
	return this.vertice1;
    }
    
    
    public Vertice getVertice2()
    {
	return this.vertice2;
    }

   
    public int getPeso()
    {
	return this.peso;
    }

    public void setPeso(int peso)
    {
	this.peso = peso;
    }

 
    public int compareTo(Arista arista2)
    {
	return this.peso - arista2.peso;
    }

 
    public String toString()
    {
	return "({" + this.vertice1 + ", " + this.vertice2  + "}, "+ this.peso  +")";
    }

    public int hashCode()
    {
	return (vertice1.getEtiqueta() + vertice2.getEtiqueta()).hashCode();
    }

     
    public boolean equals(Arista objeto)
    {
	if(!(objeto instanceof Arista))
	    return false;

	Arista arista2 = (Arista) objeto;

	if(arista2.vertice1.equals(this.vertice1) && arista2.vertice2.equals(this.vertice2))
	    return true;

	return false;
    }
}

class Grafo{

    private HashMap<String, Vertice> vertices;
    private HashMap<Integer, Arista> aristas;

    
    public Grafo()
    {
	this.vertices = new HashMap<String, Vertice>();
	this.aristas = new HashMap<Integer, Arista>();
    }


    public Grafo(ArrayList<Vertice> vertices)
    {
	this.vertices = new HashMap<String, Vertice>();
	this.aristas = new HashMap<Integer, Arista>();

	for(Vertice v : vertices )
	    {
		this.vertices.put(v.getEtiqueta(), v);
	    }
	
    }


   
    public boolean insertarArista(Vertice v1, Vertice v2)
    {
      	return insertarArista(v1, v2, 1);
    }

    public boolean insertarArista(Vertice v1, Vertice v2, int peso)
    {
	if(v1.equals(v2)) //vertices identicos?
	    return false;

	Arista arista = new Arista(v1, v2, peso);

	if(aristas.containsKey(arista.hashCode())) //arista ya está en el grafo?
	    return false;
	else if( v1.contieneUnVecino(arista) || v2.contieneUnVecino(arista)) //arista ya une a v1 o v2?
	    return false;

	aristas.put(arista.hashCode(), arista);
	v1.insertarVecino(arista);
	v2.insertarVecino(arista);
	return true;
    }

    public boolean contieneLaArista(Arista arista)
    {
	if(arista.getVertice1() == null || arista.getVertice2() == null)
	    return false;
	return this.aristas.containsKey(arista.hashCode());
    }


    
    public Arista eliminarArista(Arista arista)
    {
	arista.getVertice1().eliminarVecino(arista);
	arista.getVertice2().eliminarVecino(arista);
	return this.aristas.remove(arista.hashCode());
    }

       
    public boolean contieneElVertice(Vertice vertice)
    {
	return (this.vertices.get(vertice.getEtiqueta()) != null);
    }

    
    public Vertice getVertice(String etiqueta)
    {
	return this.vertices.get(etiqueta);
    }

    
    public boolean insertarVertice(Vertice vertice, boolean sobreescribeVertice)
    {
	Vertice actual = this.vertices.get(vertice.getEtiqueta());
	if(actual != null) //existía previamente?
	    {
		if(!sobreescribeVertice)
		    return false;

		while(actual.getContarVecinos() >= 0)
		    this.eliminarArista(actual.getVecino(0));
		
	    }

	vertices.put(vertice.getEtiqueta(), vertice);
	return true;
    }

    
    public Vertice eliminarVertice(String etiqueta)
    {
	Vertice vertice = vertices.remove(etiqueta);

	while(vertice.getContarVecinos() >= 0)
	    this.eliminarArista(vertice.getVecino(0));

	return vertice;
    }

   
    public Set<String> verticeKeys()
    {
	return this.vertices.keySet();
    }

    public Set<Arista> getAristas()
    {
	return new HashSet<Arista>(this.aristas.values());
    }
    
}

class MiGrafo{
    public static void main (String [] args){
	Grafo miGrafo = new Grafo();

	Vertice [] vertices = new Vertice[6];
	char [] etiquetas = {'A','B','C','D','E','F'};
	int i = 0;
	boolean sobreescribe = true;
	
	for(i = 0; i < vertices.length; i++)
	    {
	    	vertices[i] = new Vertice(Character.toString(etiquetas[i]));
	     	System.out.println(vertices[i]);	    	
	    }

	System.out.println();

	miGrafo.insertarArista(vertices[0], vertices[1], 3); // A -> B
	miGrafo.insertarArista(vertices[0], vertices[2], 3); // A -> C
	miGrafo.insertarArista(vertices[1], vertices[2], 1); // B -> C
	
	miGrafo.insertarArista(vertices[3], vertices[4], 3); // D -> E
	miGrafo.insertarArista(vertices[3], vertices[5], 3); // D -> F
	miGrafo.insertarArista(vertices[4], vertices[5], 1); // E -> F

	miGrafo.insertarArista(vertices[0], vertices[3], 3); // A -> D
	miGrafo.insertarArista(vertices[2], vertices[4], 2); // C -> E

	//Sacamos las adyacencias de cada vértice
	for(i = 0; i < vertices.length; i++)
	    {
		System.out.println(vertices[i]); // Representación String de la clase Vertice para este objeto

		for( int k = 0; k < vertices[i].getContarVecinos(); k++)
		    System.out.println(vertices[i].getVecino(k)); // Respresentacion String de Arista para este objeto
	    }

	//Eliminamos las adyacencias entre B <-> C
	
	for(Arista arista : vertices[1].getVecinos())
	    {
		if(arista.getVertice2().getEtiqueta().equals("C"))
		    miGrafo.eliminarArista(arista);
	    }

	for(Arista arista : vertices[2].getVecinos())
	    {
		if(arista.getVertice2().getEtiqueta().equals("B"))
		    miGrafo.eliminarArista(arista);
	    }

	//Y las adyacencias entre E <-> F
	
	for(Arista arista : vertices[4].getVecinos())
	    {
		if(arista.getVertice2().getEtiqueta().equals("E"))
		    miGrafo.eliminarArista(arista);
	    }

	for(Arista arista : vertices[5].getVecinos())
	    {
		if(arista.getVertice2().getEtiqueta().equals("F"))
		    miGrafo.eliminarArista(arista);
	    }
	
	
	System.out.println("Los vertices {B, C} tienen adyacencia: "
			   + miGrafo.contieneLaArista(new Arista( vertices[1], vertices[2] ) ) );

	System.out.println("Los vertices {E, F} tienen adyacencia: "
			   + miGrafo.contieneLaArista(new Arista (vertices[4], vertices[5] ) ) );

	
	//Sacamos nuevamente las adyacencias y los triángulos originales han perdido sus bases
	for(i = 0; i < vertices.length; i++)
	    {
	     	System.out.println(vertices[i]);
	    	
	    	for( int k = 0; k < vertices[i].getContarVecinos(); k++)
	    	    System.out.println(vertices[i].getVecino(k));
	    }	
    }
}
class Vertice
{
 
    private ArrayList<Arista> vecindad;
    private String etiqueta;

    public Vertice (String etiqueta)
    {
	this.etiqueta = etiqueta;
	this.vecindad = new ArrayList<Arista>();
    }

    
    public void insertarVecino(Arista arista)
    {
	if( !this.vecindad.contains(arista))
	    this.vecindad.add(arista);
    }

  
    public boolean contieneUnVecino(Arista arista)
    {
	return this.vecindad.contains(arista);
    }

    public Arista getVecino(int indice)
    {
	return this.vecindad.get(indice);
    }

    public Arista eliminarVecino(int indice)
    {
	return this.vecindad.remove(indice);
    }


   
    public void eliminarVecino(Arista arista)
    {
	this.vecindad.remove(arista);
    }

  
    public int getContarVecinos()
    {
	return this.vecindad.size();
    }

    
    public String getEtiqueta()
    {
	return this.etiqueta;
    }

 
   
    public boolean equals(Object vertice2)
    {
	if( !(vertice2 instanceof Vertice))
	    return false;

	Vertice v = (Vertice) vertice2;
	return this.etiqueta.equals(v.etiqueta);
    }


    public String toString()
    {
	return "Vertice: " + this.etiqueta;
    }

    public int hashCode()
    {
	return this.getEtiqueta().hashCode();
    }
    
    public ArrayList<Arista> getVecinos()
    {
	return new ArrayList<Arista>(this.vecindad);
    }
}

	
	

