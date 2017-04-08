/**
 * Copyright (C) 2017 Cristian Gomez Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.iyubinest.transactionviewer.collections.graph;

import java.util.ArrayList;

/**
 * This class models a vertex in a graph. For ease of
 * the reader, a label for this vertex is required.
 * Note that the Graph object only accepts one Vertex per label,
 * so uniqueness of labels is important. This vertex's neighborhood
 * is described by the Edges incident to it.
 *
 * @author Michael Levet
 * @date June 09, 2015
 */
public class Vertex {

  private ArrayList<Edge> neighborhood;
  private String label;

  /**
   * @param label The unique label associated with this Vertex
   */
  public Vertex(String label) {
    this.label = label;
    this.neighborhood = new ArrayList<Edge>();
  }

  /**
   * This method adds an Edge to the incidence neighborhood of this graph iff
   * the edge is not already present.
   *
   * @param edge The edge to add
   */
  public void addNeighbor(Edge edge) {
    if (this.neighborhood.contains(edge)) {
      return;
    }
    this.neighborhood.add(edge);
  }

  /**
   * @param other The edge for which to search
   * @return true iff other is contained in this.neighborhood
   */
  public boolean containsNeighbor(Edge other) {
    return this.neighborhood.contains(other);
  }

  /**
   * @param index The index of the Edge to retrieve
   * @return Edge The Edge at the specified index in this.neighborhood
   */
  public Edge getNeighbor(int index) {
    return this.neighborhood.get(index);
  }

  /**
   * @param index The index of the edge to remove from this.neighborhood
   * @return Edge The removed Edge
   */
  Edge removeNeighbor(int index) {
    return this.neighborhood.remove(index);
  }

  /**
   * @param e The Edge to remove from this.neighborhood
   */
  public void removeNeighbor(Edge e) {
    this.neighborhood.remove(e);
  }

  /**
   * @return int The number of neighbors of this Vertex
   */
  public int getNeighborCount() {
    return this.neighborhood.size();
  }

  /**
   * @return String The label of this Vertex
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * @return The hash code of this Vertex's label
   */
  public int hashCode() {
    return this.label.hashCode();
  }

  /**
   * @param other The object to compare
   * @return true iff other instanceof Vertex and the two Vertex objects have the same label
   */
  public boolean equals(Object other) {
    if (!(other instanceof Vertex)) {
      return false;
    }
    Vertex v = (Vertex) other;
    return this.label.equals(v.label);
  }

  /**
   * @return String A String representation of this Vertex
   */
  public String toString() {
    return "Vertex " + label;
  }

  /**
   * @return ArrayList<Edge> A copy of this.neighborhood. Modifying the returned
   * ArrayList will not affect the neighborhood of this Vertex
   */
  public ArrayList<Edge> getNeighbors() {
    return new ArrayList<Edge>(this.neighborhood);
  }
}

