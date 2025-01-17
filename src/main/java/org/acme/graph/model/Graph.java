package org.acme.graph.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.acme.graph.errors.NotFoundException;
import org.locationtech.jts.geom.Coordinate;

/**
 * 
 * Un graphe matérialisé par une liste de sommets et d'arcs
 * 
 * @author MBorne
 *
 */
public class Graph {
	/**
	 * Liste des sommets
	 */
	private List<Vertex> vertices = new ArrayList<>();

	/**
	 * Liste des arcs
	 */
	private List<Edge> edges = new ArrayList<>();

	/**
	 * Récupération de la liste sommets
	 * 
	 * @return
	 */
	public Collection<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Recherche d'un sommet par identifiant
	 * 
	 * @param id
	 * @return
	 */
	public Vertex findVertex(String id) {
		for (Vertex vertex : vertices) {
			if (vertex.getId().equals(id)) {
				return vertex;
			}
		}
		throw new NotFoundException(String.format("Vertex '%s' not found", id));
	}

	/**
	 * Recherche d'un sommet par égalité stricte de coordonnées
	 * 
	 * @param coordinate
	 * @return
	 */
	public Vertex findVertex(Coordinate coordinate) {
		for (Vertex vertex : vertices) {
			Coordinate candidate = vertex.getCoordinate();
			if (candidate != null && candidate.equals(coordinate)) {
				return vertex;
			}
		}
		throw new NotFoundException(String.format("Vertex not found at [%s,%s]", coordinate.x, coordinate.y));
	}

	/**
	 * Récupération ou création d'un sommet en assurant l'unicité
	 * 
	 * @param graph
	 * @param coordinate
	 * @return
	 */
	public Vertex getOrCreateVertex(Coordinate coordinate) {
		Vertex vertex;
		try {
			vertex = findVertex(coordinate);
		} catch (NotFoundException e) {
			/* création d'un nouveau sommet car non trouvé */
			vertex = new Vertex();
			vertex.setId(Integer.toString(getVertices().size()));
			vertex.setCoordinate(coordinate);
			vertices.add(vertex);
		}
		return vertex;
	}

	/**
	 * Récupération de la liste des arcs
	 * 
	 * @return
	 */
	public Collection<Edge> getEdges() {
		return edges;
	}

	/**
	 * Recherche des arcs entrant dans un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	@JsonIgnore
	public List<Edge> getInEdges(Vertex vertex) {
		return vertex.getInEdge();
	}

	/**
	 * Recherche des arcs sortant d'un sommet
	 * 
	 * @param vertex
	 * @return
	 */
	@JsonIgnore
	public List<Edge> getOutEdges(Vertex vertex) {
		return vertex.getOutEdge();
	}

	/**
	 * Creation d'un Vertex et l'associe au graph
	 *
	 * @param coordinate
	 * @param id
	 * @return
	 */
	public Vertex createVertex(Coordinate coordinate, String id){
		Vertex result = new Vertex();
		result.setId(id);
		result.setCoordinate(coordinate);
		this.getVertices().add(result);
		return result;
	}

	/**
	 * Creation d'un Edge et l'associe au graph
	 *
	 * @param source
	 * @param target
	 * @param id
	 * @return
	 */
	public Edge createEdge(Vertex source, Vertex target, String id){
		Edge result = new Edge(source, target);
		result.setId(id);
		this.getEdges().add(result);
		return result;
	}

}
