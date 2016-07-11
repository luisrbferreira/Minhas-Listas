package br.com.maktubsolutions.minhaslistas.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.model.Usuario;

/**
 * Created by luis.ferreira on 13/06/2016.
 */
public class CadastroActivity extends CommonActivity implements DatabaseReference.CompletionListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private Usuario usuario;
    private AutoCompleteTextView name;
    private AutoCompleteTextView celular;

    private FloatingActionButton FabCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cadastro");

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser == null || usuario.getId() != null) {
                    return;
                }

                usuario.setId(firebaseUser.getUid());
                usuario.saveDB(CadastroActivity.this);
            }
        };

        initViews();

        FabCadastrar = (FloatingActionButton) findViewById(R.id.fab_enviarDados_Cadastro);
        FabCadastrar.setOnClickListener(this);
    }

    protected void initViews() {
        name = (AutoCompleteTextView) findViewById(R.id.edt_Nome_Cadastro);
        email = (AutoCompleteTextView) findViewById(R.id.edt_Email_Cadastro);
        celular = (AutoCompleteTextView) findViewById(R.id.edt_Celular_Cadastro);
        password = (AutoCompleteTextView) findViewById(R.id.edt_Senha_Cadastro);
        progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
    }

    protected void initUsuario() {
        usuario = new Usuario();
        usuario.setName(name.getText().toString());
        usuario.setEmail(email.getText().toString());
        usuario.setCelular(celular.getText().toString());
        usuario.setPassword(password.getText().toString());
    }

    @Override
    public void onClick(View v) {
        initUsuario();

        String NOME = name.getText().toString();
        String EMAIL = email.getText().toString();
        String CELULAR = celular.getText().toString();
        String SENHA = password.getText().toString();

        boolean ok = true;

        if (NOME.isEmpty()) {
            name.setError(getString(R.string.msg_erro_nome));
            ok = false;
        }

        if (EMAIL.isEmpty()) {
            email.setError(getString(R.string.msg_erro_email_empty));
            ok = false;
        }

        if (CELULAR.isEmpty()) {
            celular.setError(getString(R.string.msg_erro_celular));
            ok = false;
        }

        if (SENHA.isEmpty()) {
            password.setError(getString(R.string.msg_erro_senha_empty));
            ok = false;
        }

        if (ok) {
            FabCadastrar.setEnabled(false);
            progressBar.setFocusable(true);

            openProgressBar();
            salvarUsuario();
        } else {
            closeProgressBar();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void salvarUsuario() {

        mAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    closeProgressBar();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackbar(e.getMessage());
                FabCadastrar.setEnabled(true);
            }
        });
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        mAuth.signOut();

        showToast("Conta criada com sucesso!");
        closeProgressBar();
        finish();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}