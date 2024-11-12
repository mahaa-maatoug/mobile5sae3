package com.example.sinistre;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.sinistre.entity.Sinistre;

import java.util.List;

public class SinistreAdapter extends RecyclerView.Adapter<SinistreAdapter.SinistreViewHolder> {

  private List<Sinistre> sinistreList;
  private OnItemClickListener listener;

  // Constructor with click listener
  public SinistreAdapter(List<Sinistre> sinistreList, OnItemClickListener listener) {
    this.sinistreList = sinistreList;
    this.listener = listener;
  }

  // ViewHolder class
  public static class SinistreViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate, tvLieu, tvDescription;
    public Button btnViewDetails;

    public SinistreViewHolder(View itemView) {
      super(itemView);
      tvDate = itemView.findViewById(R.id.tvdateSinistre);
      tvLieu = itemView.findViewById(R.id.tvNomSinistre);
      tvDescription = itemView.findViewById(R.id.tvDescriptionSinistre);
      btnViewDetails = itemView.findViewById(R.id.btnViewSinistre);
    }
  }

  @Override
  public SinistreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Inflate the item layout for each sinistre
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sinistre, parent, false);
    return new SinistreViewHolder(view);
  }

  @Override
  public void onBindViewHolder(SinistreViewHolder holder, int position) {
    Sinistre currentSinistre = sinistreList.get(position);
    holder.tvDate.setText(currentSinistre.getDateSinistre());
    holder.tvLieu.setText(currentSinistre.getNomSinistre());
    holder.tvDescription.setText(currentSinistre.getDescriptionSinistre());

    // Handle click event for the details button
    holder.btnViewDetails.setOnClickListener(v -> {
      if (listener != null) {
        listener.onItemClick(currentSinistre); // Pass the current sinistre to the listener
      }
    });
  }

  @Override
  public int getItemCount() {
    return sinistreList.size();
  }

  // Define the listener interface for item clicks
  public interface OnItemClickListener {
    void onItemClick(Sinistre sinistre);
  }
}
