package com.example.constat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.constat.entity.Constat;

import java.util.List;

public class ConstatAdapter extends RecyclerView.Adapter<ConstatAdapter.ConstatViewHolder> {

  private List<Constat> constatList;
  private OnItemClickListener listener;

  // Constructor with click listener
  public ConstatAdapter(List<Constat> constatList, OnItemClickListener listener) {
    this.constatList = constatList;
    this.listener = listener;
  }

  // ViewHolder class
  public static class ConstatViewHolder extends RecyclerView.ViewHolder {
    public TextView tvDate, tvLieu, tvDescription;
    public Button btnViewDetails;

    public ConstatViewHolder(View itemView) {
      super(itemView);
      tvDate = itemView.findViewById(R.id.tvDateAccident);
      tvLieu = itemView.findViewById(R.id.tvLieuAccident);
      tvDescription = itemView.findViewById(R.id.tvDescriptionAccident);
      btnViewDetails = itemView.findViewById(R.id.btnViewConstat);
    }
  }

  @Override
  public ConstatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    // Inflate the item layout for each constat
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_constat, parent, false);
    return new ConstatViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ConstatViewHolder holder, int position) {
    Constat currentConstat = constatList.get(position);
    holder.tvDate.setText(currentConstat.getDateAccident());
    holder.tvLieu.setText(currentConstat.getLieuAccident());
    holder.tvDescription.setText(currentConstat.getDescriptionAccident());

    // Handle click event for the details button
    holder.btnViewDetails.setOnClickListener(v -> {
      if (listener != null) {
        listener.onItemClick(currentConstat); // Pass the current constat to the listener
      }
    });
  }

  @Override
  public int getItemCount() {
    return constatList.size();
  }

  // Define the listener interface for item clicks
  public interface OnItemClickListener {
    void onItemClick(Constat constat);
  }
}
