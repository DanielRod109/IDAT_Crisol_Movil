package com.example.proyecto_movil_crisol.ui

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.proyecto_movil_crisol.databinding.FragmentPerfilBinding
import com.example.proyecto_movil_crisol.viewmodel.PerfilViewModel
import com.example.proyecto_movil_crisol.viewmodel.ResultadoBuscar
import com.example.proyecto_movil_crisol.viewmodel.ResultadoEditar
import mensaje

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!


    private lateinit var perfilViewModel: PerfilViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        perfilViewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilViewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        val sharedPreferences = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
        val idCliente = sharedPreferences.getInt("idCliente", 0)

        if (idCliente != 0) {
            perfilViewModel.buscarCliente(idCliente)
        }

        //cargar los datos del cliente al iniciar la vista
        perfilViewModel.resultadoBuscar.observe(viewLifecycleOwner, Observer { resultado ->
            when (resultado) {
                is ResultadoBuscar.Exito -> {
                    // mostrar datos del cliente en los inputs
                    val cliente = resultado.cliente
                    binding.edtNombres2.setText(cliente.nombres)
                    binding.edtApellidos2.setText(cliente.apellidos)
                    binding.edtDni2.setText(cliente.dni)
                    binding.edtTelefono2.setText(cliente.telefono)
                    binding.edtCorreo2.setText(cliente.email)
                    binding.edtDireccion2.setText(cliente.direccion)

                }
                is ResultadoBuscar.Error -> {
                    requireContext().mensaje(resultado.message)
                }
            }
        })

        //cerrar sesion
        binding.btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar la sesión?")
                .setPositiveButton("Sí") { dialog, which ->
                    // Eliminar los datos de la sesión
                    val sharedPrefer =
                        requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE)
                    with(sharedPrefer.edit()) {
                        remove("isUserLogged")
                        remove("nombreCliente")
                        remove("idCliente")
                        apply()
                    }

                    // Navegar al LoginActivity
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)

                    // Cerrar la actividad actual
                    requireActivity().finish()
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        //mostrar modo edicion
        binding.btnEditar.setOnClickListener {
            // Copia los datos actuales a los campos editables
            binding.edtNombres2Edit.setText(binding.edtNombres2.text)
            binding.edtApellidos2Edit.setText(binding.edtApellidos2.text)
            binding.edtDni2Edit.setText(binding.edtDni2.text)
            binding.edtTelefono2Edit.setText(binding.edtTelefono2.text)
           binding.edtDireccion2Edit.setText(binding.edtDireccion2.text)

            // Cambia la visibilidad de las vistas
            binding.layoutLectura.visibility = View.GONE
            binding.layoutEdicion.visibility = View.VISIBLE
        }
        //guardar datos editados, mostrar vista solo lectura
        binding.btnGuardar.setOnClickListener {
            perfilViewModel.resultadoBuscar.value?.let { resultado ->
                if (resultado is ResultadoBuscar.Exito) {
                    // Obtiene el cliente actual
                    val clienteActual = resultado.cliente

                    // Crea el nuevo cliente basado en el actual, pero con los datos actualizados
                    val clienteEditado = clienteActual.copy(
                        nombres = binding.edtNombres2Edit.text.toString(),
                        apellidos = binding.edtApellidos2Edit.text.toString(),
                        dni = binding.edtDni2Edit.text.toString(),
                        telefono = binding.edtTelefono2Edit.text.toString(),
                        direccion = binding.edtDireccion2Edit.text.toString(),
                        email = clienteActual.email,
                        password = clienteActual.password,
                        estado = clienteActual.estado
                    )
                    perfilViewModel.editarCliente(clienteEditado, idCliente)
                }
            }
            perfilViewModel.resultadoEditar.observe(viewLifecycleOwner, Observer { resultado ->
                when (resultado) {
                    is ResultadoEditar.Exito -> {
                        // Mostrar el mensaje
                        requireContext().mensaje("Datos actualizados correctamente!")

                        // Actualiza la vista del perfil
                        perfilViewModel.buscarCliente(idCliente)

                        // Cambia la visibilidad de las vistas
                        binding.layoutLectura.visibility = View.VISIBLE
                        binding.layoutEdicion.visibility = View.GONE
                    }
                    is ResultadoEditar.Error -> {
                        requireContext().mensaje(resultado.message)
                    }
                }
            })
        }


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

        title.text = "Mi perfil"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}