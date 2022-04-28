package com.example.projeto_ac2.ui.activities;

import static com.example.projeto_ac2.ui.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_ac2.DAO.PersonagemDAO;
import com.example.projeto_ac2.R;
import com.example.projeto_ac2.model.Personagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaPersonagemActivity extends AppCompatActivity
{
    public static final String TITULO_APPBAR = "Lista de Personagens";
    private final PersonagemDAO dao = new PersonagemDAO();
    private ArrayAdapter<Personagem> adapter;

    /*
        Faz o setup da activity.
    */
    @Override
    protected void onCreate(@Nullable Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_lista_personagem); // Põe a página XML na tela.
        setTitle(TITULO_APPBAR); // Define título na barra superior.
        configuraFabNovoPersonagem(); // Define função do floatingActionButton.
        configuraLista(); // Preenche lista e habilita context menu.
    }

    /*
        Define função do floatingActionButton criado no XML:
        Roda o método 'abreFormulario'
    */
    private void configuraFabNovoPersonagem()
    {
        FloatingActionButton botaoNovoPersonagem = findViewById(R.id.fab_add); // Pega referência ao botão no XML.
        botaoNovoPersonagem.setOnClickListener // Define função que o botão deve rodar quando pressionado.
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    abreFormulario();
                }
            }
        );
    }

    /*
        Abre a tela de formulário pra registro de personagem.
    */
    private void abreFormulario()
    {
        startActivity(new Intent(this, FormularioPersonagemActivity.class));
    }

    /*
        Atualiza lista de personagens quando entra novamente nessa activity.
    */
    @Override
    protected void onResume()
    {
        super.onResume();
        atualizaPersonagem();
    }

    /*
        Atualiza lista de personagens.
    */
    private void atualizaPersonagem()
    {
        adapter.clear();
        adapter.addAll(dao.todos());
    }

    /*
        Deleta personagem do registro o remove da lista.
    */
    private void remove(Personagem personagem)
    {
        dao.remove(personagem);
        adapter.remove(personagem);
    }

    /*
        Cria o context menu para deletar personagem.
    */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.activity_lista_personagem_menu, menu);
    }

    /*
        Define funcionamento do contextMenu.
    */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_lista_personagem_menu_remover) // Ao clicar no botão de remover personagem
        {
            /*
                Coloca menu de alerta na tela pedindo que o usuário confirme
                se deseja mesmo deletar o personagem.
                Cria os botões para confirmar ou não e define suas funções.
            */
            new AlertDialog.Builder(this)
                    .setTitle("Removendo Personagem")
                    .setMessage("Tem certeza que quer remover?")
                    .setPositiveButton
                    (
                    "Sim", new DialogInterface.OnClickListener() // Define função do botão.
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                /*
                                    Pega referência ao personagem selecionado na lista.
                                    Roda o método para remover o personagem selecionado.
                                */
                                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                Personagem personagemEscolhido = adapter.getItem(menuInfo.position);
                                remove(personagemEscolhido);
                            }
                        }
                    )
                    .setNegativeButton("Não", null) // Botão negativo não executa nenhuma função.
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    /*
        Setup da lista.
    */
    private void configuraLista()
    {
        ListView listaDePersonagens = findViewById(R.id.activity_main_lista_personagem); // Pega referência à lista no XML.
        configuraAdapter(listaDePersonagens); // Define como são mostrados itens da lista. (?)
        configuraItemPorClique(listaDePersonagens); // Define função ao clicar em item na lista.
        registerForContextMenu(listaDePersonagens); // Habilita context menu ao selecionar item da lista.
    }

    /*
        Define função a ser executada quando usuário clicar num item da lista.
    */
    private void configuraItemPorClique(ListView listaDePersonagens)
    {
        listaDePersonagens.setOnItemClickListener
        (
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id)
                {
                    Personagem personagemEscolhido = (Personagem) adapterView.getItemAtPosition(posicao);
                    abreFormularioEditar(personagemEscolhido);
                }
            }
        );
    }

    /*
        Abre a tela pra registro de personagem.
        Passa o personagem selecionado para atualizar registro.
    */
    private void abreFormularioEditar(Personagem personagemEscolhido)
    {
        Intent vaiParaFormulario = new Intent(ListaPersonagemActivity.this, FormularioPersonagemActivity.class);
        vaiParaFormulario.putExtra(CHAVE_PERSONAGEM, personagemEscolhido);
        startActivity(vaiParaFormulario);
    }

    /*
        Adapter define como serão apresentados os itens na lista. (?)
    */
    private void configuraAdapter(ListView listaDePersonagens)
    {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaDePersonagens.setAdapter(adapter);
    }
}
