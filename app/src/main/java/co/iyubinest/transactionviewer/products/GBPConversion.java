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
package co.iyubinest.transactionviewer.products;

import co.iyubinest.transactionviewer.collections.Edge;
import co.iyubinest.transactionviewer.collections.Graph;
import co.iyubinest.transactionviewer.collections.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class GBPConversion {

  private final HashMap<String, Double> rates;

  public GBPConversion(List<HttpProductsInteractor.RateResponse> rates) {
    this.rates = buildMap(buildGraph(rates));
  }

  private HashMap<String, Double> buildMap(Graph graph) {
    HashMap<String, Double> rates = new HashMap<>();
    rates.put("GBP", 1d);
    ArrayList<Edge> edges = graph.getVertex("GBP").getNeighbors();
    ArrayList<Edge> pendingEdges = new ArrayList<>();
    pendingEdges.addAll(edges);
    while (pendingEdges.size() > 0) {
      Edge edge = pendingEdges.get(0);
      double weight = edge.getWeight();
      if (!rates.containsKey(edge.getOne().getLabel())) {
        rates.put(edge.getOne().getLabel(), weight);
        for (Edge edgeOne : edge.getOne().getNeighbors()) {
          edgeOne.setWeight(edgeOne.getWeight() * weight);
          pendingEdges.add(edgeOne);
        }
      }
      if (!rates.containsKey(edge.getTwo().getLabel())) {
        rates.put(edge.getTwo().getLabel(), weight);
        for (Edge edgeTwo : edge.getTwo().getNeighbors()) {
          edgeTwo.setWeight(edgeTwo.getWeight() * weight);
          pendingEdges.add(edgeTwo);
        }
      }
      pendingEdges.remove(0);
    }
    return rates;
  }

  private Graph buildGraph(List<HttpProductsInteractor.RateResponse> rates) {
    Graph graph = new Graph();
    for (HttpProductsInteractor.RateResponse rate : rates) {
      Vertex from = graph.getVertex(rate.from);
      if (from == null) {
        from = new Vertex(rate.from);
        graph.addVertex(from, false);
      }
      Vertex to = graph.getVertex(rate.to);
      if (to == null) {
        to = new Vertex(rate.to);
        graph.addVertex(to, false);
      }
      graph.addEdge(from, to, Double.valueOf(rate.rate));
    }
    return graph;
  }

  public Double convert(Double amount, String currency) {
    return rates.get(currency) * amount;
  }
}
