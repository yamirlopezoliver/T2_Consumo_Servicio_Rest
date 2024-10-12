package com.example.serviciorestt2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.serviciorestt2.entity.Productos;
import com.example.serviciorestt2.service.UserService;
import com.example.serviciorestt2.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Al crear el spinner se crean tres objectos
    Spinner spnProductos;
    ArrayAdapter<String> adaptadorProductos;
    ArrayList<String> listaProductos = new ArrayList<String>();

    Button btnFiltrar;
    TextView txtResultado;

    //Servicio de producto de retrofit
    UserService userService;

    //Aqui estaran toda la data de productos
    List<Productos> lstSalida ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorProductos = new ArrayAdapter<String>(
                this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listaProductos);
        spnProductos = findViewById(R.id.spnProductos);
        spnProductos.setAdapter(adaptadorProductos);

        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtResultado = findViewById(R.id.txtResultado);

        userService = ConnectionRest.getConnecion().create(UserService.class);

        cargaProductos();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idProducto = (int) spnProductos.getSelectedItemId();
                Productos objProducto = lstSalida.get(idProducto);
                String salida = "Productos: ";
                salida += "Title :" + objProducto.getTitle() +" \n";
                salida += "Price :" + objProducto.getPrice() +" \n";
                salida += "Descripcion :" + objProducto.getDescription() +" \n";
                salida += "Categoría :" + objProducto.getCategory() +" \n";
                salida += "Image :" + objProducto.getImage() +" \n";
                salida += "Rating => Rate :" + objProducto.getRating().getRate() +" \n";
                salida += "Rating => Count :" + objProducto.getRating().getCount()+" \n";
             txtResultado.setText(salida);

            }
       });

    }

    void cargaProductos(){
        Call<List<Productos>> call = userService.listaProductos();
        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                //La respuestas del Servicio Rest en exitosa
                if (response.isSuccessful()){
                    lstSalida = response.body();
                    for (Productos obj: lstSalida){
                        listaProductos.add(obj.getTitle());
                    }
                    adaptadorProductos.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                //No hay respuesta del Servicio Rest
            }
        });

    }

}