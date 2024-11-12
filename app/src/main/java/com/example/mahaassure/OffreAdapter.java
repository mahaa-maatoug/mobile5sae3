package com.example.mahaassure;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mahaassure.entity.Offre;

import java.util.List;

public class OffreAdapter extends RecyclerView.Adapter<OffreAdapter.OffreViewHolder> {

  private List<Offre> offreList;
  private OnItemClickListener listener;

  // Constructor with click listener
  public OffreAdapter(List<Offre> offreList, OnItemClickListener listener) {
    this.offreList = offreList;
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
    Offre currentOffre = offreList.get(position);
    holder.tvTitle.setText(currentOffre.getType());  // Assuming 'getTitle()' should be 'getType()'
    holder.tvDescription.setText(currentOffre.getDescription());

    // If you don't have an image resource, you can comment out the line below or set a placeholder
    // holder.ivOffreImage.setImageResource(currentOffre.getImageResId());

    // Handle click event for the description button
    holder.btnViewDescription.setOnClickListener(v -> {
      listener.onItemClick(currentOffre); // Pass the current offer to the listener
    });
  }

  @Override
  public int getItemCount() {
    return offreList.size();
  }

  // Define the listener interface for item clicks
  public interface OnItemClickListener {
    void onItemClick(Offre offre);
  }
}
