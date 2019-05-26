package com.ppfmagno.palitinho;

import android.os.Handler;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    SeekBar barraDeEscolhas;
    Button botaoOk;
    ImageView maoJogador;
    ImageView maoCPU;
    TextView resultadoPartida;
    TextView textoOrientacao;
    TextView quantidadePalitinhosTexto;
    TextView palpiteTexto;
    TextView palpiteCPUTexto;
    int palitinhosJogador;
    int palitinhosCPU;
    int totalPalitinhos;
    int palpiteJogador;
    int palpiteCPU;
    int maoFechada = R.drawable.mao_fechada;
    int[] maosPalitos = {
            R.drawable.mao_aberta_0,
            R.drawable.mao_aberta_1,
            R.drawable.mao_aberta_2,
            R.drawable.mao_aberta_3
    };
    int tempoAnimacao = 600;
    boolean estaAnimando = false;
    Random r = new Random();
    Interpolator customInterpolator = PathInterpolatorCompat
            .create(.5f,-0.15f,0f,1f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barraDeEscolhas = findViewById(R.id.barraDeEscolhas);
        botaoOk = findViewById(R.id.okBtn);
        maoJogador = findViewById(R.id.maoJogador);
        maoCPU = findViewById(R.id.maoCPU);
        resultadoPartida = findViewById(R.id.resultadoPartida);
        textoOrientacao = findViewById(R.id.textoOrientacao);
        quantidadePalitinhosTexto = findViewById(R.id.quantidadePalitnhos);
        palpiteTexto = findViewById(R.id.palpite);
        palpiteCPUTexto = findViewById(R.id.palpiteCPU);

        iniciarPartida();
    }

    SeekBar.OnSeekBarChangeListener mudarPalitinhos = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            palitinhosJogador = progress;
            quantidadePalitinhosTexto.setText(String.valueOf(palitinhosJogador));
            palpiteTexto.setText(String.valueOf(palitinhosJogador));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    SeekBar.OnSeekBarChangeListener mudarPalpite = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            palpiteJogador = progress + palitinhosJogador;
            palpiteTexto.setText(String.valueOf(palpiteJogador));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    View.OnClickListener definirPalitinhos = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ajustarViewParaPalpite();
            // Define palitinhos da CPU
            palitinhosCPU = r.nextInt(4);
        }
    };

    View.OnClickListener definirPalpite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Checa se a animação está em curso, caso esteja, não deve fazer nada
            if (!estaAnimando) {
                // Limita o palpite da CPU entre a quantidade de palitos e o máximo possível
                palpiteCPU = r.nextInt(4) + palitinhosCPU;
                palpiteCPUTexto.setText(String.valueOf(palpiteCPU));
                finalizarPartida();
            }
        }
    };

    // Serve para reiniciar a partida após o fim
    View.OnClickListener reiniciarPartida = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            iniciarPartida();
        }
    };

    // Ajusta view e escutadores de eventos par o início da partida
    void iniciarPartida() {
        palitinhosJogador = 0;
        palpiteJogador = 0;

        resultadoPartida.setText("");
        textoOrientacao.setText("Escolha a quantidade de palitos:");
        quantidadePalitinhosTexto.setText("0");
        palpiteTexto.setText("0");
        palpiteCPUTexto.setText("");

        maoJogador.setImageResource(maoFechada);
        maoCPU.setImageResource(maoFechada);

        barraDeEscolhas.setProgress(0);
        barraDeEscolhas.setOnSeekBarChangeListener(mudarPalitinhos);

        // Escutador de click é definido para a escolha de palitos (mudará posteriormente)
        botaoOk.setOnClickListener(definirPalitinhos);
    }

    // Ajusta view e escutadores de eventos par a escolha de palpites
    void ajustarViewParaPalpite() {
        textoOrientacao.setText("Dê seu palpite:");

        barraDeEscolhas.setProgress(0);
        barraDeEscolhas.setOnSeekBarChangeListener(mudarPalpite);

        // Escutador de click é definido para a escolha do palpite
        botaoOk.setOnClickListener(definirPalpite);
    }

    // Anima as mãos, mostra o resultado e depois seta escutador para reinício de partida
    void finalizarPartida() {
        animarMaos();

        // Espera o tempo de animação para mostrar o resultado poder habilitar o recomeço
        // evita que o usuário comece uma outra partida antes do fim da anterior
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarResultado();
                textoOrientacao.setText("Toque ok para recomeçar:");
                botaoOk.setOnClickListener(reiniciarPartida);
                estaAnimando = !estaAnimando;
            }
        }, tempoAnimacao);
    }

    // Define animationSet para cada mão e anima as mãos
    void animarMaos() {
        // Definição da animação da mão do jogador
        RotateAnimation rotateMaoJogadorIda = new RotateAnimation(
                0, 30,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 2f);
        rotateMaoJogadorIda.setDuration(tempoAnimacao/2);

        RotateAnimation rotateMaoJogadorVolta = new RotateAnimation(
                0, -30,
                Animation.RELATIVE_TO_SELF, 1f,
                Animation.RELATIVE_TO_SELF, 2f);
        rotateMaoJogadorVolta.setDuration(tempoAnimacao/2);
        rotateMaoJogadorVolta.setStartOffset(tempoAnimacao/2);

        AnimationSet animacaoMaoJogador = new AnimationSet(true);
        animacaoMaoJogador.setFillAfter(true);
        animacaoMaoJogador.setInterpolator(customInterpolator);
        animacaoMaoJogador.addAnimation(rotateMaoJogadorIda);
        animacaoMaoJogador.addAnimation(rotateMaoJogadorVolta);

        // Definição da animação da mão da CPU
        RotateAnimation rotateMaoCPUIda = new RotateAnimation(
                0, 30,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);
        rotateMaoCPUIda.setDuration(tempoAnimacao/2);

        RotateAnimation rotateMaoCPUVolta = new RotateAnimation(
                0, -30,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -1f);
        rotateMaoCPUVolta.setDuration(tempoAnimacao/2);
        rotateMaoCPUVolta.setStartOffset(tempoAnimacao/2);

        AnimationSet animacaoCPU = new AnimationSet(true);
        animacaoCPU.setFillAfter(true);
        animacaoCPU.setInterpolator(customInterpolator);
        animacaoCPU.addAnimation(rotateMaoCPUIda);
        animacaoCPU.addAnimation(rotateMaoCPUVolta);

        // Anima as mãos
        maoCPU.startAnimation(animacaoCPU);
        maoJogador.startAnimation(animacaoMaoJogador);
        estaAnimando = !estaAnimando;

        // Espera o tempo de animação menos 100ms para trocar as imagens (dá a sensação de movimento real)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                maoJogador.setImageResource(maosPalitos[palitinhosJogador]);
                maoCPU.setImageResource(maosPalitos[palitinhosCPU]);
            }
        }, tempoAnimacao - 100);
    }

    // Mostra o texto de resultado da prtida
    void mostrarResultado() {
        if (quemGanhou().equals("jogador")) resultadoPartida.setText("Você venceu! :)");
        else if (quemGanhou().equals("cpu")) resultadoPartida.setText("Você perdeu! :(");
        else resultadoPartida.setText("Empatou!");
    }

    // Define vencedor a partir da soma dos palitos e comparando com o palpite
    String quemGanhou() {
        totalPalitinhos = palitinhosJogador + palitinhosCPU;
        // Se os dois tiverem acertados, ninguém ganha
        if (palpiteJogador == totalPalitinhos && palpiteCPU == totalPalitinhos) return "ninguem";
        if (palpiteJogador == totalPalitinhos) return "jogador";
        if (palpiteCPU == totalPalitinhos) return "cpu";
        // Se ninguém tiver acertado, ninguém ganha
        return "ninguem";
    }
}
