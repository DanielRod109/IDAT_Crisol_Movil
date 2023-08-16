package com.example.proyecto_movil_crisol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyecto_movil_crisol.databinding.ActivityPrincipalBinding
import com.example.proyecto_movil_crisol.model.Producto
import com.example.proyecto_movil_crisol.ui.CatalagoFragment

class ProductoAdapter (
    public var productos: List<Producto>,
    private val onClickListener: (Producto) -> Unit

    ) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

        // ViewHolder que representa un solo ítem en el RecyclerView
        class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imagen: ImageView = itemView.findViewById(R.id.imagen)
            val nombre: TextView = itemView.findViewById(R.id.nombre)
            val autor: TextView = itemView.findViewById(R.id.autor)
            val precio: TextView = itemView.findViewById(R.id.precio)
        }

        // infla la vista de ítem y crea el ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
            return ProductoViewHolder(view)
        }

        // n° total de ítems
        override fun getItemCount(): Int = productos.size

        // Vincula los datos del modelo con la vista
        override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
            val producto = productos[position]
            val precio = producto.precio.toString()

            Glide.with(holder.itemView.context)
                .load(producto.img) // asumiendo que `producto.imagen` es una URL
                .placeholder(R.drawable.cargando) // una imagen mientras la imagen principal se está cargando
                .into(holder.imagen)


            holder.nombre.text = producto.nombre
            holder.autor.text = producto.nombre_autor
            holder.precio.text = "S/. "+precio

            holder.itemView.setOnClickListener {
                onClickListener(producto)
            }
        }

        fun updateData(newProductos: List<Producto>) {
            productos = newProductos
            notifyDataSetChanged()  // Notificar al adaptador que los datos han cambiado
        }
    }



