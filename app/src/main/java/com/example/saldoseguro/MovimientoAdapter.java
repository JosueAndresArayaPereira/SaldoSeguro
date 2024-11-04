package com.example.saldoseguro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovimientoAdapter extends RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder> {
    private List<movimiento> movimientos;

    public MovimientoAdapter(List<movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    @NonNull
    @Override
    public MovimientoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento, parent, false);
        return new MovimientoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovimientoViewHolder holder, int position) {
        movimiento movimiento = movimientos.get(position);
        holder.categoriaTextView.setText(movimiento.getCategoria());
        holder.cantidadTextView.setText(String.valueOf(movimiento.getCantidad()));
        holder.fechaTextView.setText(movimiento.getFecha());
        holder.cuentaTextView.setText(movimiento.getCuenta());
        holder.tipoMovimientoTextView.setText(movimiento.getTipoMovimiento());
    }

    @Override
    public int getItemCount() {
        return movimientos.size();
    }

    public void updateMovimientos(List<movimiento> nuevosMovimientos) {
        this.movimientos.clear();
        this.movimientos.addAll(nuevosMovimientos);
        notifyDataSetChanged();
    }

    // ViewHolder interno para optimizar el rendimiento
    public static class MovimientoViewHolder extends RecyclerView.ViewHolder {
        TextView categoriaTextView;
        TextView cantidadTextView;
        TextView fechaTextView;
        TextView cuentaTextView;
        TextView tipoMovimientoTextView;

        public MovimientoViewHolder(@NonNull View itemView) {
            super(itemView);
            categoriaTextView = itemView.findViewById(R.id.textViewCategoria);
            cantidadTextView = itemView.findViewById(R.id.textViewCantidad);
            fechaTextView = itemView.findViewById(R.id.textViewFecha);
        }
    }
}
