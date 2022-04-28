package com.example.projeto_ac2.DAO;

import com.example.projeto_ac2.model.Personagem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/*
    Guarda personagens registrados.
*/
public class PersonagemDAO
{
    private final static List<Personagem> personagens = new ArrayList<>();
    private static int contadorDeIds = 1;

    /*
        Registra personagem na lista.
    */
    public void salva(Personagem personagemSalvo)
    {
        personagemSalvo.setId(contadorDeIds);
        personagens.add(personagemSalvo);
        atualizaId();
    }

    private void atualizaId()
    {
        contadorDeIds++;
    }

    /*
        Substitui na lista o personagem que foi editado.
    */
    public void edita(Personagem personagem)
    {
        Personagem personagemEncontrado = buscaPersonagemId(personagem);
        if(personagemEncontrado != null)
        {
            int posicaoDoPersonagem = personagens.indexOf(personagemEncontrado);
            personagens.set(posicaoDoPersonagem, personagem);
        }
    }

    /*
        Busca personagem na lista.
    */
    private Personagem buscaPersonagemId(Personagem personagem)
    {
        for(Personagem p : personagens)
        {
            if(p.getId() == personagem.getId())
            {
                return p;
            }
        }
        return null;
    }

    /*
        Retorna lista dos personagens registrados.
    */
    public List<Personagem> todos()
    {
        return new ArrayList<>(personagens);
    }

    /*
        Remoeve personagem da lista de personagens registrados.
    */
    public void remove(Personagem personagem)
    {
        Personagem personagemDevolvido = buscaPersonagemId(personagem);
        if(personagemDevolvido != null)
        {
            personagens.remove(personagemDevolvido);
        }
    }
}
