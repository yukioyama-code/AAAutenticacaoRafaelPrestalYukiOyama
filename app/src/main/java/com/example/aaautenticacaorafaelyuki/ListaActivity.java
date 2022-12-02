package com.example.aaautenticacaorafaelyuki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaActivity extends AppCompatActivity {

    private TextView lblUsuario;
    private EditText txtNome;
    private EditText txtNota1;
    private EditText txtNota2;
    private Button btnInserir;
    private Button btnSair;
    private ListView lista;

    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth usuarios = FirebaseAuth.getInstance();


    private AlunosAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lblUsuario = findViewById(R.id.lblUsuario);
        txtNome = findViewById(R.id.txtNome);
        txtNota1 = findViewById(R.id.txtNota1);
        txtNota2 = findViewById(R.id.txtNota2);
        btnInserir = findViewById(R.id.btnInserir);
        btnSair = findViewById(R.id.btnSair);
        lista = findViewById(R.id.lista);

        DatabaseReference atividadeAutenticacao = BD.child("alunoLista");
        FirebaseListOptions<Aluno> options = new FirebaseListOptions.Builder<Aluno>().setLayout(R.layout.item_lista).setQuery(atividadeAutenticacao, Aluno.class).setLifecycleOwner(this).build();

        adapter = new AlunosAdapter(options);

        lista.setAdapter(adapter);


        lblUsuario.setText(usuarios.getCurrentUser().getEmail());

        btnInserir.setOnClickListener(new EscutadorInserir());

        btnSair.setOnClickListener(new EscutadorSair());


    }
    @Override
    public void onBackPressed() {
        // Vazio pra desabilitar o back
    }



    private class EscutadorInserir implements View.OnClickListener {
        @Override

        public void onClick(View view){
            String nome, nota1, nota2;
            double nota1d, nota2d;

            DatabaseReference atividadeAutenticacao = BD.child("alunoLista");

            nome = txtNome.getText().toString();
            nota1 = txtNota1.getText().toString();
            nota2 = txtNota2.getText().toString();

            nota1d = Double.parseDouble(nota1);
            nota2d = Double.parseDouble(nota2);


            Aluno a = new Aluno(nome, nota1d, nota2d);

            String chave = atividadeAutenticacao.push().getKey();

            atividadeAutenticacao.child(chave).setValue(a);

            txtNome.setText("");
            txtNota1.setText("");
            txtNota2.setText("");

            txtNome.requestFocus();

        }

    }
    private class AlunosAdapter extends FirebaseListAdapter<Aluno> {

        public AlunosAdapter(FirebaseListOptions options) {
            super(options);
        }



        @Override
        protected void populateView(View v, Aluno a, int position) {


            TextView lblNome = v.findViewById( R.id.lblNome );
            TextView lblNota1 = v.findViewById( R.id.lblNota1);
            TextView lblNota2 = v.findViewById(R.id.lblNota2);
            TextView lblMedia = v.findViewById(R.id.lblMedia);


            double media = (a.getNota1()+a.getNota2())/2;
            String medias = Double.toString(media);
            String nota1s = Double.toString(a.getNota1());
            String nota2s = Double.toString(a.getNota2());
            lblNome.setText(a.getNome());
            lblNota1.setText(nota1s);
            lblNota2.setText(nota1s);
            lblMedia.setText(medias);






        }

    }






    private class EscutadorSair implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            // Aviso de início de processo:
            Toast.makeText(ListaActivity.this, "Tentando deslogar do firebase..." , Toast.LENGTH_SHORT).show();


            // Verifica se existe um usuário logado:
            if ( usuarios.getCurrentUser() == null ) {

                // Exibe mensagem que não tem usuário logado, em lblEstado:
                Toast.makeText(ListaActivity.this, "Não tem usuário logado." , Toast.LENGTH_SHORT).show();
            }
            else {

                // Existe usuário logado.

                // Deslogando...
                usuarios.signOut();

                // Exibe mensagem de usuário deslogado:
                Toast.makeText(ListaActivity.this, "Usuário deslogado." , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }







}
