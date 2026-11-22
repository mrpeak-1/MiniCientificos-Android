package com.icsm.minicientificos_android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.cart.CartManager;
import com.icsm.minicientificos_android.databinding.FragmentVideojuegosBinding;
import com.icsm.minicientificos_android.models.game.BancoPreguntasCatalog;
import com.icsm.minicientificos_android.models.game.NivelJuego;
import com.icsm.minicientificos_android.models.game.PreguntaCientifica;
import com.icsm.minicientificos_android.utils.CientificoVoiceHelper;
import com.icsm.minicientificos_android.utils.GameProgressManager;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VideojuegosFragment extends Fragment {

    private FragmentVideojuegosBinding binding;

    // ESTADO DE LA PARTIDA
    private List<NivelJuego> niveles;
    private NivelJuego nivelSeleccionado;
    private List<PreguntaCientifica> preguntasPartida;
    private int indicePreguntaActual = 0;
    private PreguntaCientifica preguntaActual;

    private int vidasRestantes = 3;
    private int rachaCombo = 0;
    private int puntajePartida = 0;
    private int respuestasCorrectasCount = 0;

    private CountDownTimer timerPregunta;
    private long tiempoRestanteMs = 15000;
    private boolean timerPausado = false;

    // ESTADO DE COMODINES
    private boolean comodin5050Usado = false;
    private boolean comodinCongelarUsado = false;
    private boolean comodinPistaUsado = false;

    private final Random random = new Random();

    public VideojuegosFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentVideojuegosBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        niveles = BancoPreguntasCatalog.obtenerNiveles();

        setupLobbyListeners();
        setupArenaListeners();
        setupResultadoListeners();

        actualizarPerfilJugadorLobby();
        renderizarTarjetasMundos();
        mostrarVistaLobby();
    }

    // ==========================================
    // VISTA A: LOBBY DE SELECCIÓN DE MUNDOS
    // ==========================================

    private void mostrarVistaLobby() {
        if (binding == null) return;
        detenerCronometroSeguro();
        binding.layoutLobbyJuego.setVisibility(View.VISIBLE);
        binding.layoutArenaPartida.setVisibility(View.GONE);
        binding.layoutResultadoPartida.setVisibility(View.GONE);
        actualizarPerfilJugadorLobby();
        renderizarTarjetasMundos();
    }

    private void setupLobbyListeners() {
        binding.btnLobbyMedallas.setOnClickListener(v -> {
            VitrinaMedallasBottomSheet dialog = new VitrinaMedallasBottomSheet();
            dialog.show(getChildFragmentManager(), "VitrinaMedallasBottomSheet");
        });

        binding.btnLobbyCarnet.setOnClickListener(v -> {
            GeneradorCredencialBottomSheet dialog = new GeneradorCredencialBottomSheet();
            dialog.show(getChildFragmentManager(), "GeneradorCredencialBottomSheet");
        });

        binding.btnLobbyShakeLab.setOnClickListener(v -> {
            LaboratorioShakeBottomSheet dialog = new LaboratorioShakeBottomSheet();
            dialog.show(getChildFragmentManager(), "LaboratorioShakeBottomSheet");
        });
    }

    private void actualizarPerfilJugadorLobby() {
        if (getContext() == null || binding == null) return;

        GameProgressManager gpm = GameProgressManager.getInstance(requireContext());
        String nombre = gpm.getNombreAgenteGuardado();
        int xp = gpm.getXpTotal();
        String rango = gpm.getRangoCientificoActual();
        int record = gpm.getRecordPuntaje();

        binding.tvLobbyAgenteNombre.setText("👤 Agente: " + nombre);
        binding.tvLobbyRango.setText("🏆 Rango: " + rango);
        binding.tvLobbyXpTotal.setText("⚡ " + xp + " XP");
        binding.tvLobbyRecord.setText("🎯 Récord: " + record + " pts");
    }

    private void renderizarTarjetasMundos() {
        if (getContext() == null || binding == null || niveles == null) return;

        binding.containerMundosList.removeAllViews();
        GameProgressManager gpm = GameProgressManager.getInstance(requireContext());

        for (NivelJuego nivel : niveles) {
            MaterialCardView card = new MaterialCardView(requireContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(0, 0, 0, 24);
            card.setLayoutParams(lp);
            card.setRadius(32);
            card.setCardElevation(6);
            card.setStrokeWidth(2);

            try {
                card.setCardBackgroundColor(Color.parseColor(nivel.getColorHex()));
                card.setStrokeColor(Color.parseColor(nivel.getColorHex()));
            } catch (Throwable ignored) {
                card.setCardBackgroundColor(Color.parseColor("#0B2C5E"));
                card.setStrokeColor(Color.parseColor("#FDC500"));
            }

            LinearLayout content = new LinearLayout(requireContext());
            content.setOrientation(LinearLayout.VERTICAL);
            content.setPadding(28, 24, 28, 24);

            int estrellasGanadas = gpm.getEstrellasNivel(nivel.getId());
            StringBuilder sbEstrellas = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                if (i < estrellasGanadas) {
                    sbEstrellas.append("⭐");
                } else {
                    sbEstrellas.append("☆");
                }
            }

            TextView tvHeader = new TextView(requireContext());
            tvHeader.setText(nivel.getIconoEmoji() + " " + nivel.getTitulo() + "  " + sbEstrellas.toString());
            tvHeader.setTextColor(Color.WHITE);
            tvHeader.setTextSize(18);
            tvHeader.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView tvSub = new TextView(requireContext());
            tvSub.setText(nivel.getNumeroMundo() + ": " + nivel.getDescripcion());
            tvSub.setTextColor(Color.parseColor("#E2E8F0"));
            tvSub.setTextSize(12);
            tvSub.setPadding(0, 8, 0, 12);

            MaterialButton btnJugar = new MaterialButton(requireContext());
            btnJugar.setText("▶ ¡JUGAR MISIÓN! (Premio: Cupón " + nivel.getCodigoCuponPremio() + ")");
            btnJugar.setTextColor(Color.parseColor("#0B2C5E"));
            btnJugar.setBackgroundColor(Color.parseColor("#FDC500"));
            btnJugar.setCornerRadius(24);
            btnJugar.setTextSize(13);

            btnJugar.setOnClickListener(v -> iniciarPartidaEnMundo(nivel));

            content.addView(tvHeader);
            content.addView(tvSub);
            content.addView(btnJugar);

            card.addView(content);
            binding.containerMundosList.addView(card);
        }
    }

    // ==========================================
    // VISTA B: ARENA DE PARTIDA EN TIEMPO REAL
    // ==========================================

    private void iniciarPartidaEnMundo(NivelJuego nivel) {
        this.nivelSeleccionado = nivel;
        this.vidasRestantes = 3;
        this.rachaCombo = 0;
        this.puntajePartida = 0;
        this.respuestasCorrectasCount = 0;
        this.indicePreguntaActual = 0;

        this.comodin5050Usado = false;
        this.comodinCongelarUsado = false;
        this.comodinPistaUsado = false;

        this.preguntasPartida = BancoPreguntasCatalog.obtenerPreguntasPorNivel(nivel.getId());
        if (preguntasPartida == null || preguntasPartida.isEmpty()) {
            preguntasPartida = BancoPreguntasCatalog.obtenerTodasLasPreguntas();
        }

        resetearBotonesComodinesUI();

        binding.layoutLobbyJuego.setVisibility(View.GONE);
        binding.layoutArenaPartida.setVisibility(View.VISIBLE);
        binding.layoutResultadoPartida.setVisibility(View.GONE);

        cargarPreguntaPartidaActual();
    }

    private void resetearBotonesComodinesUI() {
        if (binding == null) return;
        binding.btnComodin5050.setEnabled(true);
        binding.btnComodin5050.setAlpha(1f);

        binding.btnComodinCongelar.setEnabled(true);
        binding.btnComodinCongelar.setAlpha(1f);

        binding.btnComodinPista.setEnabled(true);
        binding.btnComodinPista.setAlpha(1f);
    }

    private void setupArenaListeners() {
        binding.btnExitArena.setOnClickListener(v -> mostrarVistaLobby());

        binding.btnToggleAudioArena.setOnClickListener(v -> {
            GameSoundAndVfxHelper.getInstance().toggleSonido();
            boolean activo = GameSoundAndVfxHelper.getInstance().isSonidoActivo();
            Toast.makeText(requireContext(), activo ? "🔊 Sonido activado" : "🔇 Sonido silenciado", Toast.LENGTH_SHORT).show();
        });

        binding.btnLeerPreguntaArena.setOnClickListener(v -> leerPreguntaEnVozAlta());

        binding.btnOpcionA.setOnClickListener(v -> evaluarRespuestaSeleccionada(0));
        binding.btnOpcionB.setOnClickListener(v -> evaluarRespuestaSeleccionada(1));
        binding.btnOpcionC.setOnClickListener(v -> evaluarRespuestaSeleccionada(2));
        binding.btnOpcionD.setOnClickListener(v -> evaluarRespuestaSeleccionada(3));

        binding.btnEscucharCapsulaVoz.setOnClickListener(v -> {
            if (preguntaActual != null && getContext() != null) {
                CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla(preguntaActual.getExplicacionSabiasQue());
            }
        });

        binding.btnSiguientePreguntaArena.setOnClickListener(v -> avanzarSiguientePreguntaOFinalizar());

        // COMODINES
        binding.btnComodin5050.setOnClickListener(v -> activarComodin5050());
        binding.btnComodinCongelar.setOnClickListener(v -> activarComodinCongelar());
        binding.btnComodinPista.setOnClickListener(v -> activarComodinPista());
    }

    private void cargarPreguntaPartidaActual() {
        if (binding == null || preguntasPartida == null || preguntasPartida.isEmpty()) return;

        if (indicePreguntaActual >= preguntasPartida.size()) {
            finalizarPartidaExitosamente();
            return;
        }

        preguntaActual = preguntasPartida.get(indicePreguntaActual);

        actualizarHudArena();
        actualizarBuddyCharacter();

        binding.tvPreguntaNumero.setText("Pregunta " + (indicePreguntaActual + 1) + " de " + preguntasPartida.size());
        binding.tvPreguntaCategoria.setText(preguntaActual.getCategoria());
        binding.tvPreguntaTexto.setText(preguntaActual.getPregunta());

        String[] ops = preguntaActual.getOpciones();
        if (ops.length >= 4) {
            binding.btnOpcionA.setText("A) " + ops[0]);
            binding.btnOpcionB.setText("B) " + ops[1]);
            binding.btnOpcionC.setText("C) " + ops[2]);
            binding.btnOpcionD.setText("D) " + ops[3]);
        }

        resetearEstiloOpcionesArena();
        binding.cardCapsulaEducativa.setVisibility(View.GONE);

        iniciarCronometroPregunta(15000);
    }

    private void actualizarHudArena() {
        if (binding == null) return;
        StringBuilder sbVidas = new StringBuilder();
        for (int i = 0; i < vidasRestantes; i++) {
            sbVidas.append("❤️");
        }
        binding.tvCorazonesVida.setText(sbVidas.toString());
        binding.tvComboRacha.setText("🔥 Racha x" + rachaCombo);
        binding.tvPuntajePartida.setText(puntajePartida + " Pts");
    }

    private void actualizarBuddyCharacter() {
        if (binding == null || getContext() == null) return;

        boolean esTesla = (nivelSeleccionado != null && nivelSeleccionado.getId() % 2 == 0);
        String personaje = esTesla ? "TESLA" : "TITAN";
        String estado = (rachaCombo >= 2) ? "CELEBRANDO" : "FELIZ";

        int resId = GameSoundAndVfxHelper.obtenerDrawablePersonajeEstado(requireContext(), personaje, estado);
        binding.ivBuddyAvatar.setImageResource(resId);

        if (esTesla) {
            binding.tvBuddyBubble.setText("¡Hola, futuro genio! Tesla te acompaña en este experimento.");
        } else {
            binding.tvBuddyBubble.setText("¡Titan listo para el despegue científico! Elige tu respuesta.");
        }
    }

    private void resetearEstiloOpcionesArena() {
        if (binding == null) return;
        int colorNormal = Color.parseColor("#F8FAFC");
        int colorTexto = Color.parseColor("#0F172A");

        binding.btnOpcionA.setBackgroundColor(colorNormal);
        binding.btnOpcionB.setBackgroundColor(colorNormal);
        binding.btnOpcionC.setBackgroundColor(colorNormal);
        binding.btnOpcionD.setBackgroundColor(colorNormal);

        binding.btnOpcionA.setTextColor(colorTexto);
        binding.btnOpcionB.setTextColor(colorTexto);
        binding.btnOpcionC.setTextColor(colorTexto);
        binding.btnOpcionD.setTextColor(colorTexto);

        binding.btnOpcionA.setEnabled(true);
        binding.btnOpcionB.setEnabled(true);
        binding.btnOpcionC.setEnabled(true);
        binding.btnOpcionD.setEnabled(true);

        binding.btnOpcionA.setAlpha(1f);
        binding.btnOpcionB.setAlpha(1f);
        binding.btnOpcionC.setAlpha(1f);
        binding.btnOpcionD.setAlpha(1f);
    }

    private void iniciarCronometroPregunta(long duracionMs) {
        detenerCronometroSeguro();
        this.tiempoRestanteMs = duracionMs;

        if (binding == null) return;
        binding.progressTimerPregunta.setMax(15);
        binding.progressTimerPregunta.setProgress((int) (duracionMs / 1000));

        timerPregunta = new CountDownTimer(duracionMs, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestanteMs = millisUntilFinished;
                int segs = (int) (millisUntilFinished / 1000);

                if (binding != null) {
                    binding.progressTimerPregunta.setProgress(segs);
                    binding.tvSegundosRestantes.setText(segs + "s");

                    if (segs <= 4) {
                        binding.tvSegundosRestantes.setTextColor(Color.parseColor("#EF4444"));
                    } else if (segs <= 8) {
                        binding.tvSegundosRestantes.setTextColor(Color.parseColor("#EAB308"));
                    } else {
                        binding.tvSegundosRestantes.setTextColor(Color.parseColor("#0B2C5E"));
                    }
                }
            }

            @Override
            public void onFinish() {
                if (binding != null) {
                    binding.tvSegundosRestantes.setText("0s!");
                    evaluarRespuestaPorTiempoAgotado();
                }
            }
        }.start();
    }

    private void detenerCronometroSeguro() {
        if (timerPregunta != null) {
            timerPregunta.cancel();
            timerPregunta = null;
        }
    }

    private void evaluarRespuestaSeleccionada(int indiceOpcion) {
        detenerCronometroSeguro();
        deshabilitarOpcionesPartida();

        if (preguntaActual == null || binding == null || getContext() == null) return;

        boolean esCorrecta = (indiceOpcion == preguntaActual.getIndiceRespuestaCorrecta());

        if (esCorrecta) {
            rachaCombo++;
            respuestasCorrectasCount++;
            int puntosGanados = 100 + (rachaCombo * 20);
            puntajePartida += puntosGanados;

            resaltarBotonOpcion(indiceOpcion, "#16A34A", Color.WHITE);
            GameSoundAndVfxHelper.getInstance().playRespuestaCorrecta(requireContext());
            GameSoundAndVfxHelper.animarPulsoExito(binding.cardPreguntaArena);

            binding.tvBuddyBubble.setText("🎉 ¡EXCELENTE DEDUCCIÓN! + " + puntosGanados + " Pts");
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan("¡Respuesta correcta! Excelente razonamiento científico.");
        } else {
            rachaCombo = 0;
            vidasRestantes--;

            resaltarBotonOpcion(indiceOpcion, "#EF4444", Color.WHITE);
            resaltarBotonOpcion(preguntaActual.getIndiceRespuestaCorrecta(), "#16A34A", Color.WHITE);

            GameSoundAndVfxHelper.getInstance().playRespuestaIncorrecta(requireContext());
            GameSoundAndVfxHelper.animarSacudidaError(binding.cardPreguntaArena);

            binding.tvBuddyBubble.setText("❌ ¡Casi! Revisa la cápsula educativa abajo.");
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla("¡Oh no! Pero no te preocupes, de los errores se aprende en la ciencia.");
        }

        actualizarHudArena();
        mostrarCapsulaEducativa();
    }

    private void evaluarRespuestaPorTiempoAgotado() {
        deshabilitarOpcionesPartida();
        if (preguntaActual == null || binding == null || getContext() == null) return;

        rachaCombo = 0;
        vidasRestantes--;

        resaltarBotonOpcion(preguntaActual.getIndiceRespuestaCorrecta(), "#16A34A", Color.WHITE);

        GameSoundAndVfxHelper.getInstance().playRespuestaIncorrecta(requireContext());
        GameSoundAndVfxHelper.animarSacudidaError(binding.cardPreguntaArena);

        binding.tvBuddyBubble.setText("⏰ ¡TIEMPO AGOTADO! Se restó 1 vida.");
        actualizarHudArena();
        mostrarCapsulaEducativa();
    }

    private void deshabilitarOpcionesPartida() {
        if (binding == null) return;
        binding.btnOpcionA.setEnabled(false);
        binding.btnOpcionB.setEnabled(false);
        binding.btnOpcionC.setEnabled(false);
        binding.btnOpcionD.setEnabled(false);
    }

    private void resaltarBotonOpcion(int indice, String colorHexBg, int colorTexto) {
        if (binding == null) return;
        MaterialButton btn = null;
        if (indice == 0) btn = binding.btnOpcionA;
        else if (indice == 1) btn = binding.btnOpcionB;
        else if (indice == 2) btn = binding.btnOpcionC;
        else if (indice == 3) btn = binding.btnOpcionD;

        if (btn != null) {
            btn.setBackgroundColor(Color.parseColor(colorHexBg));
            btn.setTextColor(colorTexto);
        }
    }

    private void mostrarCapsulaEducativa() {
        if (binding == null || preguntaActual == null) return;

        binding.tvExplicacionCapsula.setText(preguntaActual.getExplicacionSabiasQue());
        binding.cardCapsulaEducativa.setVisibility(View.VISIBLE);
        GameSoundAndVfxHelper.animarPulsoExito(binding.cardCapsulaEducativa);
    }

    private void leerPreguntaEnVozAlta() {
        if (preguntaActual != null && getContext() != null) {
            boolean esTesla = (nivelSeleccionado != null && nivelSeleccionado.getId() % 2 == 0);
            if (esTesla) {
                CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla(preguntaActual.getPregunta());
            } else {
                CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan(preguntaActual.getPregunta());
            }
        }
    }

    private void avanzarSiguientePreguntaOFinalizar() {
        if (vidasRestantes <= 0) {
            finalizarPartidaPorDerrota();
            return;
        }

        indicePreguntaActual++;
        if (indicePreguntaActual >= preguntasPartida.size()) {
            finalizarPartidaExitosamente();
        } else {
            cargarPreguntaPartidaActual();
        }
    }

    // ==========================================
    // COMODINES CIENTÍFICOS
    // ==========================================

    private void activarComodin5050() {
        if (comodin5050Usado) {
            Toast.makeText(requireContext(), "🧪 Comodín 50/50 ya utilizado en esta partida", Toast.LENGTH_SHORT).show();
            return;
        }
        comodin5050Usado = true;
        binding.btnComodin5050.setEnabled(false);
        binding.btnComodin5050.setAlpha(0.5f);

        if (preguntaActual == null || binding == null) return;

        int correcta = preguntaActual.getIndiceRespuestaCorrecta();
        List<Integer> incorrectas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != correcta) {
                incorrectas.add(i);
            }
        }

        // Ocultar 2 incorrectas al azar
        int deshabilitar1 = incorrectas.remove(random.nextInt(incorrectas.size()));
        int deshabilitar2 = incorrectas.remove(random.nextInt(incorrectas.size()));

        ocultarOpcionPorComodin(deshabilitar1);
        ocultarOpcionPorComodin(deshabilitar2);

        GameSoundAndVfxHelper.getInstance().playComodinActivado();
        Toast.makeText(requireContext(), "🧪 ¡50/50 Activado! Se descartaron 2 opciones incorrectas.", Toast.LENGTH_SHORT).show();
    }

    private void ocultarOpcionPorComodin(int indice) {
        if (binding == null) return;
        MaterialButton btn = null;
        if (indice == 0) btn = binding.btnOpcionA;
        else if (indice == 1) btn = binding.btnOpcionB;
        else if (indice == 2) btn = binding.btnOpcionC;
        else if (indice == 3) btn = binding.btnOpcionD;

        if (btn != null) {
            btn.setEnabled(false);
            btn.setAlpha(0.2f);
        }
    }

    private void activarComodinCongelar() {
        if (comodinCongelarUsado) {
            Toast.makeText(requireContext(), "❄️ Comodín Congelar ya utilizado en esta partida", Toast.LENGTH_SHORT).show();
            return;
        }
        comodinCongelarUsado = true;
        binding.btnComodinCongelar.setEnabled(false);
        binding.btnComodinCongelar.setAlpha(0.5f);

        detenerCronometroSeguro();
        iniciarCronometroPregunta(tiempoRestanteMs + 10000);

        GameSoundAndVfxHelper.getInstance().playComodinActivado();
        Toast.makeText(requireContext(), "❄️ ¡Tiempo congelado! +10 segundos agregados al reloj.", Toast.LENGTH_SHORT).show();
    }

    private void activarComodinPista() {
        if (comodinPistaUsado) {
            Toast.makeText(requireContext(), "💡 Comodín Pista ya utilizado en esta partida", Toast.LENGTH_SHORT).show();
            return;
        }
        comodinPistaUsado = true;
        binding.btnComodinPista.setEnabled(false);
        binding.btnComodinPista.setAlpha(0.5f);

        if (preguntaActual != null && getContext() != null) {
            String pista = preguntaActual.getPistaTesla();
            Toast.makeText(requireContext(), "💡 Pista de Tesla: " + pista, Toast.LENGTH_LONG).show();
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla("Pista especial de Tesla: " + pista);
            GameSoundAndVfxHelper.getInstance().playComodinActivado();
        }
    }

    // ==========================================
    // VISTA C: PANTALLA DE RESULTADO Y VICTORIA
    // ==========================================

    private void finalizarPartidaExitosamente() {
        finalizarPartidaInternal(true);
    }

    private void finalizarPartidaPorDerrota() {
        finalizarPartidaInternal(false);
    }

    private void finalizarPartidaInternal(boolean victoria) {
        detenerCronometroSeguro();
        if (binding == null || getContext() == null) return;

        GameProgressManager gpm = GameProgressManager.getInstance(requireContext());

        int estrellas = 0;
        if (victoria) {
            if (respuestasCorrectasCount >= 8) {
                estrellas = 3;
            } else if (respuestasCorrectasCount >= 5) {
                estrellas = 2;
            } else {
                estrellas = 1;
            }
        }

        int xpGanado = respuestasCorrectasCount * 50;
        gpm.sumarXp(xpGanado);
        gpm.actualizarRecord(puntajePartida);

        if (nivelSeleccionado != null) {
            gpm.guardarEstrellasNivel(nivelSeleccionado.getId(), estrellas);
        }

        binding.layoutLobbyJuego.setVisibility(View.GONE);
        binding.layoutArenaPartida.setVisibility(View.GONE);
        binding.layoutResultadoPartida.setVisibility(View.VISIBLE);

        StringBuilder sbEstrellas = new StringBuilder();
        for (int i = 0; i < estrellas; i++) {
            sbEstrellas.append("⭐");
        }
        if (sbEstrellas.length() == 0) {
            sbEstrellas.append("💔");
        }

        binding.tvResultadoEstrellas.setText(sbEstrellas.toString());
        binding.tvResultadoTituloPartida.setText(victoria ? "🎉 ¡MISIÓN CIENTÍFICA COMPLETA!" : "💔 ¡MISIÓN FALLIDA! SIN VIDAS");
        binding.tvResultadoResumenPuntos.setText("Obtuviste " + puntajePartida + " Pts (" + respuestasCorrectasCount + " acertadas) · +" + xpGanado + " XP");

        // GESTIÓN DE CUPÓN DESBLOQUEABLE
        String cuponDesbloqueado = null;
        if (victoria && estrellas >= 2 && nivelSeleccionado != null) {
            cuponDesbloqueado = gpm.desbloquearCuponPorVictoria(nivelSeleccionado.getId());
        }

        if (cuponDesbloqueado != null && !cuponDesbloqueado.isEmpty()) {
            final String finalCupon = cuponDesbloqueado;
            binding.cardPremioCupon.setVisibility(View.VISIBLE);
            binding.tvCodigoCuponGanado.setText("CUPÓN: " + cuponDesbloqueado);

            binding.btnAplicarCuponCarrito.setOnClickListener(v -> {
                CartManager.getInstance().aplicarCupon(finalCupon);
                Toast.makeText(requireContext(), "🎉 ¡Cupón '" + finalCupon + "' aplicado con éxito en tu Carrito!", Toast.LENGTH_LONG).show();
            });
        } else {
            binding.cardPremioCupon.setVisibility(View.GONE);
        }

        if (victoria) {
            GameSoundAndVfxHelper.getInstance().playVictoriaNivel(requireContext());
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan("¡Misión científica completada con éxito! Has ganado " + xpGanado + " puntos de experiencia.");
        } else {
            GameSoundAndVfxHelper.getInstance().playRespuestaIncorrecta(requireContext());
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla("No te rindas. ¡Aprende de esta lección e inténtalo de nuevo!");
        }
    }

    private void setupResultadoListeners() {
        binding.btnReintentarMision.setOnClickListener(v -> {
            if (nivelSeleccionado != null) {
                iniciarPartidaEnMundo(nivelSeleccionado);
            } else if (niveles != null && !niveles.isEmpty()) {
                iniciarPartidaEnMundo(niveles.get(0));
            }
        });

        binding.btnVolverLobby.setOnClickListener(v -> mostrarVistaLobby());
    }

    @Override
    public void onPause() {
        super.onPause();
        detenerCronometroSeguro();
        if (getContext() != null) {
            CientificoVoiceHelper.getInstance(requireContext()).detenerVoz();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detenerCronometroSeguro();
        binding = null;
    }
}
