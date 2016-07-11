package br.com.maktubsolutions.minhaslistas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.model.Usuario;

public class LoginActivity extends CommonActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private Usuario usuario;
    private Button btnLogin;
    private TextView recuperarSenha;
    private TextView possuiCadastro;
    private TextView cadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = getFirebaseAuthResultHandler();

        initViews();

        btnLogin = (Button) findViewById(R.id.btn_Login);
        btnLogin.setOnClickListener(this);
    }

    protected void initViews() {

        email = (AutoCompleteTextView) findViewById(R.id.edt_Email_Login);
        password = (EditText) findViewById(R.id.edt_Senha_Login);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        recuperarSenha = (TextView) findViewById(R.id.txt_Recuperar_Senha);
        possuiCadastro = (TextView) findViewById(R.id.txt_Possui_Cadastro);
        cadastrar = (TextView) findViewById(R.id.txt_Cadastrar);
    }

    protected void initUsuario() {
        usuario = new Usuario();
        usuario.setEmail(email.getText().toString());
        usuario.setPassword(password.getText().toString());
    }

    @Override
    public void onClick(View v) {

        initUsuario();

        int id = v.getId();
        if (id == R.id.btn_Login) {

            String EMAIL = email.getText().toString();
            String SENHA = password.getText().toString();

            boolean ok = true;

            if (EMAIL.isEmpty()) {
                email.setError(getString(R.string.msg_erro_email_empty));

                ok = false;
            }

            if (SENHA.isEmpty()) {
                password.setError(getString(R.string.msg_erro_senha_empty));

                ok = false;
            }

            if (ok) {
                btnLogin.setEnabled(false);
                recuperarSenha.setEnabled(false);
                possuiCadastro.setEnabled(false);
                cadastrar.setEnabled(false);
                progressBar.setFocusable(true);

                openProgressBar();
                verifyLogin();
            } else {
                closeProgressBar();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        verifyLogged();
    }

    private void verifyLogged() {

        if (firebaseAuth.getCurrentUser() != null) {
            chamarMainActivity();
        } else {
            firebaseAuth.addAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private FirebaseAuth.AuthStateListener getFirebaseAuthResultHandler() {
        FirebaseAuth.AuthStateListener callback = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser userFirebase = firebaseAuth.getCurrentUser();

                if (userFirebase == null) {
                    return;
                }

                if (usuario.getId() == null && isNameOk(usuario, userFirebase)) {

                    usuario.setId(userFirebase.getUid());
                    usuario.setNameIfNull(userFirebase.getDisplayName());
                    usuario.setEmailIfNull(userFirebase.getEmail());
                    usuario.saveDB();
                }

                chamarMainActivity();
            }
        };
        return (callback);
    }

    private void verifyLogin() {
        usuario.saveProviderSP(LoginActivity.this, "");
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            closeProgressBar();

                            btnLogin.setEnabled(true);
                            recuperarSenha.setEnabled(true);
                            possuiCadastro.setEnabled(true);
                            cadastrar.setEnabled(true);

                            return;
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showSnackbar(connectionResult.getErrorMessage());
    }

    private boolean isNameOk(Usuario usuario, FirebaseUser firebaseUser) {
        return (usuario.getName() != null || firebaseUser.getDisplayName() != null);
    }

    private void chamarMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void chamarCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void recuperarSenha(View view) {
        Intent intent = new Intent(this, RecuperarSenhaActivity.class);
        startActivity(intent);
    }
}
