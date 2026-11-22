package com.icsm.minicientificos_android.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.BottomSheetLaboratorioShakeBinding;
import com.icsm.minicientificos_android.utils.CientificoVoiceHelper;
import com.icsm.minicientificos_android.utils.GameProgressManager;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

public class LaboratorioShakeBottomSheet extends BottomSheetDialogFragment implements SensorEventListener {

    private BottomSheetLaboratorioShakeBinding binding;

    private SensorManager sensorManager;
    private Sensor acelerometro;
    private long ultimoTiempoShake = 0;
    private int progresoActual = 0;
    private boolean reaccionCompletada = false;

    private int experimentoSeleccionado = 1; // 1: Volcán, 2: Fotocatálisis, 3: Slime, 4: Hidroponía

    public LaboratorioShakeBottomSheet() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BottomSheetLaboratorioShakeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarSensor();
        setupListeners();
        cargarExperimento(1);
    }

    private void inicializarSensor() {
        if (getContext() == null) return;
        sensorManager = (SensorManager) requireContext().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    private void setupListeners() {
        binding.btnCloseShakeLab.setOnClickListener(v -> dismiss());

        binding.chipGroupExperimentos.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) return;
            int id = checkedIds.get(0);
            if (id == R.id.chip_exp_volcan) {
                cargarExperimento(1);
            } else if (id == R.id.chip_exp_fotocatalisis) {
                cargarExperimento(2);
            } else if (id == R.id.chip_exp_slime) {
                cargarExperimento(3);
            } else if (id == R.id.chip_exp_hidroponia) {
                cargarExperimento(4);
            }
        });

        binding.btnAgitarDedo.setOnClickListener(v -> incrementarProgresoConSacudida());

        binding.btnEscucharExplicacionLab.setOnClickListener(v -> {
            if (getContext() == null || binding == null) return;
            String explicacion = binding.tvResultadoExplicacion.getText().toString();
            if (experimentoSeleccionado == 2 || experimentoSeleccionado == 4) {
                CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla(explicacion);
            } else {
                CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan(explicacion);
            }
        });

        binding.btnReiniciarExperimento.setOnClickListener(v -> reiniciarExperimentoActual());
    }

    private void cargarExperimento(int id) {
        this.experimentoSeleccionado = id;
        reiniciarExperimentoActual();

        switch (id) {
            case 1:
                binding.tvReactivoA.setText("Peróxido + Jabón");
                binding.tvReactivoB.setText("Catalizador Levadura");
                binding.tvResultadoTitulo.setText("🌋 ¡VOLCÁN CATALÍTICO COMPLETADO! (+50 XP)");
                binding.tvResultadoExplicacion.setText("¡Has liberado una espuma gigante! La levadura actuó como catalizador biológico descomponiendo el peróxido en agua y burbujas de oxígeno atrapadas en el jabón.");
                break;
            case 2:
                binding.tvReactivoA.setText("Agua Contaminada");
                binding.tvReactivoB.setText("Dióxido de Titanio + UV");
                binding.tvResultadoTitulo.setText("☀️ ¡FOTOCATÁLISIS EXITOSA! (+50 XP)");
                binding.tvResultadoExplicacion.setText("¡Agua purificada limpiamente! La luz solar activa los nanopartículas de Dióxido de Titanio (TiO2) rompiendo las moléculas de bacterias y contaminantes.");
                break;
            case 3:
                binding.tvReactivoA.setText("Polímero PVA");
                binding.tvReactivoB.setText("Activador + Óxido Hierro");
                binding.tvResultadoTitulo.setText("🧲 ¡SLIME MAGNÉTICO CREADO! (+50 XP)");
                binding.tvResultadoExplicacion.setText("¡Excelente viscosidad no newtoniana! Las cadenas de polímero se enlazan con el borato y las partículas de hierro responden a campos magnéticos.");
                break;
            case 4:
                binding.tvReactivoA.setText("Raíz Hidropónica");
                binding.tvReactivoB.setText("Sales Minerales A + B");
                binding.tvResultadoTitulo.setText("🌱 ¡SÚPER NUTRIENTE MEZCLADO! (+50 XP)");
                binding.tvResultadoExplicacion.setText("¡Fórmula hidropónica perfecta! Nitrógeno, fósforo, potasio y microelementos disueltos directamente en agua para un crecimiento 30% más rápido sin tierra.");
                break;
        }
    }

    private void reiniciarExperimentoActual() {
        this.progresoActual = 0;
        this.reaccionCompletada = false;

        if (binding == null) return;
        binding.progressReaccionQuimica.setProgress(0);
        binding.tvPorcentajeReaccion.setText("0% - ¡Mezcla sin agitar!");
        binding.cardResultadoReaccion.setVisibility(View.GONE);
        binding.ivMatrazIcon.setRotation(0f);
        binding.ivMatrazIcon.setScaleX(1f);
        binding.ivMatrazIcon.setScaleY(1f);
    }

    private void incrementarProgresoConSacudida() {
        if (reaccionCompletada || binding == null) return;

        progresoActual += 20;
        if (progresoActual > 100) {
            progresoActual = 100;
        }

        binding.progressReaccionQuimica.setProgress(progresoActual);
        binding.tvPorcentajeReaccion.setText(progresoActual + "% - ¡Reacción en proceso!");

        // Animar sacudida del matraz
        ObjectAnimator shake = ObjectAnimator.ofFloat(binding.ivMatrazIcon, View.ROTATION, 0f, 18f);
        shake.setDuration(280);
        shake.setInterpolator(new CycleInterpolator(2f));
        shake.start();

        // Reproducir efecto de sonido / háptica
        GameSoundAndVfxHelper.getInstance().playComodinActivado();

        if (progresoActual >= 100) {
            finalizarReaccionQuimicaExitosamente();
        }
    }

    private void finalizarReaccionQuimicaExitosamente() {
        if (reaccionCompletada || binding == null || getContext() == null) return;
        reaccionCompletada = true;

        binding.tvPorcentajeReaccion.setText("100% - ¡REACCIÓN QUÍMICA COMPLETADA!");
        binding.cardResultadoReaccion.setVisibility(View.VISIBLE);

        // VFX de celebración
        GameSoundAndVfxHelper.animarPulsoExito(binding.cardResultadoReaccion);
        GameSoundAndVfxHelper.getInstance().playVictoriaNivel(requireContext());

        // Sumar 50 XP
        GameProgressManager.getInstance(requireContext()).sumarXp(50);

        // Narrar por voz
        String explicacion = binding.tvResultadoExplicacion.getText().toString();
        if (experimentoSeleccionado == 2 || experimentoSeleccionado == 4) {
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla("¡Increíble! " + explicacion);
        } else {
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan("¡Misión cumplida! " + explicacion);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null || event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Calcular aceleración neta restando gravedad terrestre
        double gNeto = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

        long ahora = System.currentTimeMillis();
        if (gNeto > 11.5 && (ahora - ultimoTiempoShake > 320)) {
            ultimoTiempoShake = ahora;
            if (getActivity() != null) {
                getActivity().runOnUiThread(this::incrementarProgresoConSacudida);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No requerido
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null && acelerometro != null) {
            sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        if (getContext() != null) {
            CientificoVoiceHelper.getInstance(requireContext()).detenerVoz();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
