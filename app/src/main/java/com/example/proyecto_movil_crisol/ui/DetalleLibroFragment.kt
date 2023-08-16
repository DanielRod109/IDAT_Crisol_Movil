package com.example.proyecto_movil_crisol.ui

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.proyecto_movil_crisol.R
import com.example.proyecto_movil_crisol.databinding.FragmentDetalleLibroBinding
import com.example.proyecto_movil_crisol.viewmodel.CatalogoViewModel
import com.example.proyecto_movil_crisol.viewmodel.ResultadoBuscarLibroPorId
import mensaje


class DetalleLibroFragment : Fragment() {

    private var _binding: FragmentDetalleLibroBinding? = null
    private val binding get() = _binding!!
    private lateinit var catalogoViewModel: CatalogoViewModel

    private var idLibro: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idLibro = it.getInt("id_libro")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalleLibroBinding.inflate(inflater, container, false)
        catalogoViewModel = ViewModelProvider(this).get(CatalogoViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idLibro?.let {
            catalogoViewModel.buscarLibroPorId(it)
        }


        catalogoViewModel.resultadoBuscarLibroPorId.observe(viewLifecycleOwner, { resultado ->
            when (resultado) {
                is ResultadoBuscarLibroPorId.Exito -> {
                    val libro = resultado.producto

                    Glide.with(requireContext())
                        .load(libro.img)
                        .placeholder(R.drawable.cargando)
                        .into(binding.imageLibro)

                    binding.tvTitulo.text = libro.nombre
                    binding.tvPrecio.text = "S/" + libro.precio.toString()
                    binding.tvNombreAutor.text = libro.nombre_autor
                    binding.tvEditorial.text = libro.editorial
                    binding.tvAlto.text = libro.alto.toString() + "cm."
                    binding.tvAncho.text = libro.ancho.toString() + "cm."
                    binding.tvAnio.text = libro.aedicion

                }
                is ResultadoBuscarLibroPorId.Error -> {
                    requireContext().mensaje(resultado.message)
                }
            }
        })


        val toolbar: Toolbar = binding.toolbar
        val title: TextView = binding.toolbarTitle

        //toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), com.google.android.material.R.drawable.ic_arrow_back_black_24)
        val backButton = ContextCompat.getDrawable(requireContext(), com.google.android.material.R.drawable.ic_arrow_back_black_24)?.mutate()
        backButton?.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.white), PorterDuff.Mode.SRC_ATOP)
        toolbar.navigationIcon = backButton
        toolbar.setNavigationOnClickListener {
            view.findNavController().navigateUp()
        }

        // Establecer la Toolbar como la ActionBar para esta actividad
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        // título por defecto no se muestre
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

        title.text = "Detalle del producto"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}