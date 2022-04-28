package com.example.projeto_ac2.ui.activities;

import static com.example.projeto_ac2.ui.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.projeto_ac2.DAO.PersonagemDAO;
import com.example.projeto_ac2.R;
import com.example.projeto_ac2.model.Personagem;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class FormularioPersonagemActivity extends AppCompatActivity
{
    private static final String TITULO_APPBAR_EDIT_PERSONAGEM = "Editar o Personagem";
    private static final String TITULO_APPBAR_NEW_PERSONAGEM = "Novo Personagem";
    private EditText campoNome;
    private EditText campoNascimento;
    private EditText campoAltura;
    private final PersonagemDAO dao = new PersonagemDAO();
    private Personagem personagem;

    /*
        Cria menu para salvar personagem.
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_formulario_personagem_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
        Define função do botão no menu para finalizar o formulário.
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_personagem_menu_salvar)
        {
            finalizarFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Faz o setup da activity.
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_personagem);
        inicializarCampos();
        carregaPersonagem();
    }

    /*
        Se estiver editando personagem, carrega personagem.
        Se estiver registrando um novo, cria personagem.
    */
    protected void carregaPersonagem()
    {
        Intent dados = getIntent();
        if(dados.hasExtra(CHAVE_PERSONAGEM))
        {
            setTitle(TITULO_APPBAR_EDIT_PERSONAGEM);
            personagem = (Personagem) dados.getSerializableExtra(CHAVE_PERSONAGEM);
            preencheCampos();
        }
        else
        {
            setTitle(TITULO_APPBAR_NEW_PERSONAGEM);
            personagem = new Personagem();
        }
    }

    /*
        Preenche os campos com as propriedades do personagem sendo editado.
    */
    private void preencheCampos()
    {
        campoNome.setText(personagem.getNome());
        campoAltura.setText(personagem.getAltura());
        campoNascimento.setText(personagem.getNascimento());
    }

    /*
        Caso o personagem já for existente, edita seu registro.
        Caso for novo, registra personagem.
    */
    private void finalizarFormulario()
    {
        preencherPersonagem();
        if(personagem.idValido())
        {
            dao.edita(personagem);
            finish();
        }
        else
        {
            dao.salva(personagem);
        }
        finish();
    }

    /*
        Pega referências aos campos de input no XML.
        Restringe o texto que pode ser recebido em cada campo a um formato.
    */
    private void inicializarCampos()
    {
        campoNome = findViewById(R.id.editText_nome);
        campoNascimento = findViewById(R.id.editText_nascimento);
        campoAltura = findViewById(R.id.editText_altura);

        SimpleMaskFormatter smfAltura =  new SimpleMaskFormatter("N, NN");
        MaskTextWatcher mtwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(mtwAltura);

        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(mtwNascimento);
    }

    /*
        Preenche as propriedades do personagem sendo editado com o conteúdo dos campos de input.
    */
    private void preencherPersonagem()
    {
        String nome = campoNome.getText().toString();
        String nascimento = campoNascimento.getText().toString();
        String altura = campoAltura.getText().toString();

        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
    }
}
