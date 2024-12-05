package com.tispucminas.sistemaezsnack.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.tispucminas.sistemaezsnack.exception.UnauthorizedException;
import com.tispucminas.sistemaezsnack.model.Usuario;

public class SenhaUtilService {

    public static void verificaSenhaAtual(Usuario usuario, String senhaAntiga) throws UnauthorizedException, NoSuchAlgorithmException {
        byte[] senhaAntigaCriptografada = criptografarSenha(senhaAntiga);
        if(!Arrays.equals(senhaAntigaCriptografada, usuario.getSenha())){
            throw new UnauthorizedException("A senha antiga está incorreta!");
        }
    }

    public static byte[] criptografarSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(senha.getBytes());
    }
}
