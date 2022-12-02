package com.example.aaautenticacaorafaelyuki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class MainActivity extends AppCompatActivity {
    private EditText txtEmail;
    private EditText txtSenha;
    private Button btnLogin;
    private Button btnCriar;

    private FirebaseAuth usuarios = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnCriar = findViewById(R.id.btnCriar);
        btnLogin = findViewById(R.id.btnLogin);


        if ( usuarios.getCurrentUser() != null ) {
            Intent i = new Intent( getApplicationContext(), ListaActivity.class );
            startActivity(i);
        }

        btnLogin.setOnClickListener( new EscutadorBotaoEntrar() );
        btnCriar.setOnClickListener( new EscutadorBotaoCriar() );
    }


    private class EscutadorBotaoEntrar implements View.OnClickListener {

        @Override
        public void onClick(View view) {


            String email = txtEmail.getText().toString();
            String senha = txtSenha.getText().toString();


            usuarios.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(Task<AuthResult> task) {


                            if ( task.isSuccessful() ) {
                                Toast.makeText(MainActivity.this, "Usuário logado com sucesso: " + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent( getApplicationContext(), ListaActivity.class );
                                startActivity(i);

                            }
                            else {


                                Toast.makeText(MainActivity.this, "Login falhou! ", Toast.LENGTH_SHORT).show();


                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                Toast.makeText(MainActivity.this, "ERRO: "  + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    private class EscutadorBotaoCriar implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Toast.makeText(MainActivity.this, "Tentando criar novo usuário. ", Toast.LENGTH_SHORT).show();

            // Verifica se o usuário já está logado:
            if (usuarios.getCurrentUser() != null) {

                // Exibe mensagem de usuário já logado, em lblEstado:
                Toast.makeText(MainActivity.this, "Usuário já logado " + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            } else {

                // Usuário não logado.

                // Pega email e senha na interface:
                String email = txtEmail.getText().toString();
                String senha = txtSenha.getText().toString();

                // Tenta criar o usuário:
                // ATENCAO!!!
                // O teste se foi sucesso ou não é ASSINCRONO,
                // isto é, pode levar um tempo para o resultado voltar do Firebase.
                // Não pense neste processo como uma programação tradicional!!!
                usuarios.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(Task<AuthResult> task) {

                                // Testa se criou o usuário com sucesso:
                                if (task.isSuccessful()) {

                                    // Criou e logou com sucesso.
                                    // Exibe mensagem em lblEstado:
                                    Toast.makeText(MainActivity.this, "Usuário criado e logado com sucesso " + usuarios.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();

                                } else {

                                    // Não conseguiu criar o usuário.
                                    // Exibe mensagem em lblEstado:
                                    Toast.makeText(MainActivity.this, " Criação do usuário falhou ", Toast.LENGTH_SHORT).show();

                                    // Exibe a mensagem de erro do Firebase num Toast:
                                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                    Toast.makeText(MainActivity.this, "ERRO: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }

    }
}