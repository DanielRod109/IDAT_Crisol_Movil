package com.example.proyecto_movil_crisol.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto_movil_crisol.ProductoAdapter
import com.example.proyecto_movil_crisol.R
import com.example.proyecto_movil_crisol.databinding.FragmentCatalagoBinding
import com.example.proyecto_movil_crisol.model.Producto
import com.example.proyecto_movil_crisol.viewmodel.CatalogoViewModel
import com.example.proyecto_movil_crisol.viewmodel.ResultadoBuscarPorNombre
import com.example.proyecto_movil_crisol.viewmodel.ResultadoListar
import mensaje

class CatalagoFragment : Fragment() {

    private var _binding: FragmentCatalagoBinding? = null
    private val binding get() = _binding!!

    private var allProductos: List<Producto> = listOf()

    private lateinit var catalogoViewModel: CatalogoViewModel
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatalagoBinding.inflate(inflater, container, false)
        catalogoViewModel = ViewModelProvider(this).get(CatalogoViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //evento para que al hacer click en un producto muestre el detalle
        adapter = ProductoAdapter(emptyList(), onClickListener = { libro ->
            val idLibro = libro.id_libro // o producto.id_libro si así lo has definido
            Log.d("CatalogoFragment", "Producto seleccionado con ID: $idLibro")
            val detalleLibroFragment = DetalleLibroFragment().apply {
                arguments = Bundle().apply {
                    putInt("id_libro", idLibro)
                }
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detalleLibroFragment)  // Usas el ID del contenedor que está en tu Activity
                .addToBackStack(null)
                .commit()
        })

        //numero de columnas para los libros : 2
        binding.productosRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.productosRecyclerView.adapter = adapter

        // Observa el LiveData para la lista completa de productos
        catalogoViewModel.resultadoListar.observe(viewLifecycleOwner, { resultado ->
            when (resultado) {
                is ResultadoListar.Exito -> {
                    allProductos = resultado.libros
                    adapter.updateData(resultado.libros)
                }
                is ResultadoListar.Error -> {
                    requireContext().mensaje(resultado.message)
                }
            }
        })

        // Observa el LiveData para la búsqueda
        catalogoViewModel.resultadoBuscarPorNombre.observe(viewLifecycleOwner, { resultado ->
            when (resultado) {
                is ResultadoBuscarPorNombre.Exito -> {
                    if (resultado.libros.isEmpty()) {
                        requireContext().mensaje("No se encontraron resultados para la búsqueda.")
                        adapter.updateData(allProductos) // Muestra todos los productos
                    } else {
                        adapter.updateData(resultado.libros) // Muestra los resultados de la búsqueda
                    }
                }
                is ResultadoBuscarPorNombre.Error -> {
                    requireContext().mensaje(resultado.message)
                    adapter.updateData(allProductos) // Muestra todos los productos
                }
            }
        })

        // Configuración de la barra de búsqueda
        binding.barraBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    catalogoViewModel.buscarPorNombreAutorEditorial(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // Carga inicial de productos
        catalogoViewModel.listarProductos()


        val collapsingToolbarLayout = binding.collapsingToolbar
        collapsingToolbarLayout.title = "Crisol App"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}