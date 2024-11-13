package com.example.constat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.example.constat.entity.Offre;


import java.util.ArrayList;
import java.util.List;

public class OffreAdapter extends RecyclerView.Adapter<OffreAdapter.OffreViewHolder> {

  private List<Offre> offreList; // Liste complète des offres
  private List<Offre> filteredOffreList; // Liste filtrée pour la recherche
  private OnItemClickListener listener;

  // Constructor with click listener
  public OffreAdapter(List<Offre> offreList, OnItemClickListener listener) {
    this.offreList = offreList;
    this.filteredOffreList = new ArrayList<>(offreList); // Initialiser la liste filtrée avec toutes les offres
    this.listener = listener;
  }

  // ViewHolder class
  public static class OffreViewHolder extends RecyclerView.ViewHolder {
    public ImageView ivOffreImage;
    public TextView tvTitle, tvDescription;
    public Button btnViewDescription;

    public OffreViewHolder(View itemView) {
      super(itemView);
      ivOffreImage = itemView.findViewById(R.id.ivOffreImage); // Correct ImageView ID
      tvTitle = itemView.findViewById(R.id.tvTitle);           // Correct TextView ID for Title
      tvDescription = itemView.findViewById(R.id.tvDescription); // Correct TextView ID for Description
      btnViewDescription = itemView.findViewById(R.id.btnViewDescription); // Correct Button ID
    }
  }

  @Override
  public OffreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Inflate the item layout for each offer
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offre, parent, false);
    return new OffreViewHolder(view);
  }

  @Override
  public void onBindViewHolder(OffreViewHolder holder, int position) {
    Offre currentOffre = filteredOffreList.get(position); // Utiliser la liste filtrée
    holder.tvTitle.setText(currentOffre.getType());
    holder.tvDescription.setText(currentOffre.getDescription());

    // Handle click event for the description button
    holder.btnViewDescription.setOnClickListener(v -> {
      listener.onItemClick(currentOffre);
    });
  }

  @Override
  public int getItemCount() {
    return filteredOffreList.size(); // Retourner la taille de la liste filtrée
  }

  // Define the listener interface for item clicks
  public interface OnItemClickListener {
    void onItemClick(Offre offre);
  }

  // Method to filter the list based on a search query
  public void filter(String query) {
    filteredOffreList.clear();
    if (query.isEmpty()) {
      filteredOffreList.addAll(offreList); // Si la recherche est vide, afficher toutes les offres
    } else {
      String lowerCaseQuery = query.toLowerCase();
      for (Offre offre : offreList) {
        // Filtrer par type ou description
        if (offre.getType().toLowerCase().contains(lowerCaseQuery) ||
          offre.getDescription().toLowerCase().contains(lowerCaseQuery)) {
          filteredOffreList.add(offre);
        }
      }
    }
    notifyDataSetChanged(); // Rafraîchir l'affichage
  }
}
