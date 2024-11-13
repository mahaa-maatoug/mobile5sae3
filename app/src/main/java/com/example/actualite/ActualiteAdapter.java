package com.example.actualite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.actualite.entity.Actualite;

import java.util.List;

public class ActualiteAdapter extends RecyclerView.Adapter<ActualiteAdapter.ActualiteViewHolder> {

  private List<Actualite> actualiteList;
  private OnItemClickListener listener;

  // Constructor with click listener
  public ActualiteAdapter(List<Actualite> actualiteList, OnItemClickListener listener) {
    this.actualiteList = actualiteList;
    this.listener = listener;
  }

  // ViewHolder class
  public static class ActualiteViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate, tvLieu, tvDescription;
    public Button btnViewDetails;

    public ActualiteViewHolder(View itemView) {
      super(itemView);
      tvDate = itemView.findViewById(R.id.tvDateActualite);
      tvLieu = itemView.findViewById(R.id.tvLieuActualite);
      tvDescription = itemView.findViewById(R.id.tvDescriptionActualite);
      btnViewDetails = itemView.findViewById(R.id.btnViewActualite);
    }
  }

  @Override
  public ActualiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Inflate the item layout for each actualite
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actualite, parent, false);
    return new ActualiteViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ActualiteViewHolder holder, int position) {
    Actualite currentActualite = actualiteList.get(position);
    holder.tvDate.setText(currentActualite.getDateActualite());
    holder.tvLieu.setText(currentActualite.getTitreActualite());
    holder.tvDescription.setText(currentActualite.getDescriptionActualite());

    // Handle click event for the details button
    holder.btnViewDetails.setOnClickListener(v -> {
      if (listener != null) {
        listener.onItemClick(currentActualite); // Pass the current actualite to the listener
      }
    });
  }

  @Override
  public int getItemCount() {
    return actualiteList.size();
  }

  // Define the listener interface for item clicks
  public interface OnItemClickListener {
    void onItemClick(Actualite actualite);
  }
}
