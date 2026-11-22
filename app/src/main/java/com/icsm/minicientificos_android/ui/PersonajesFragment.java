package com.icsm.minicientificos_android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.icsm.minicientificos_android.R;
import com.icsm.minicientificos_android.databinding.FragmentPersonajesBinding;
import com.icsm.minicientificos_android.utils.CientificoVoiceHelper;
import com.icsm.minicientificos_android.utils.GameSoundAndVfxHelper;

public class PersonajesFragment extends Fragment {

    private FragmentPersonajesBinding binding;
    private boolean mostrandoTitan = true;

    public PersonajesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonajesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupToggleListeners();
        setupVoiceListeners();
        setupActionListeners();

        seleccionarTitan();
    }

    private void setupToggleListeners() {
        binding.btnSelectTitan.setOnClickListener(v -> seleccionarTitan());
        binding.btnSelectTesla.setOnClickListener(v -> seleccionarTesla());
    }

    private void seleccionarTitan() {
        if (binding == null) return;
        mostrandoTitan = true;

        binding.cardTitanFicha.setVisibility(View.VISIBLE);
        binding.cardTeslaFicha.setVisibility(View.GONE);

        // Estilos de botones de toggle
        binding.btnSelectTitan.setBackgroundColor(Color.parseColor("#FDC500"));
        binding.btnSelectTitan.setTextColor(Color.parseColor("#0B2C5E"));

        binding.btnSelectTesla.setBackgroundColor(Color.TRANSPARENT);
        binding.btnSelectTesla.setTextColor(Color.WHITE);

        GameSoundAndVfxHelper.animarPulsoExito(binding.cardTitanFicha);
    }

    private void seleccionarTesla() {
        if (binding == null) return;
        mostrandoTitan = false;

        binding.cardTitanFicha.setVisibility(View.GONE);
        binding.cardTeslaFicha.setVisibility(View.VISIBLE);

        // Estilos de botones de toggle
        binding.btnSelectTesla.setBackgroundColor(Color.parseColor("#FDC500"));
        binding.btnSelectTesla.setTextColor(Color.parseColor("#0B2C5E"));

        binding.btnSelectTitan.setBackgroundColor(Color.TRANSPARENT);
        binding.btnSelectTitan.setTextColor(Color.WHITE);

        GameSoundAndVfxHelper.animarPulsoExito(binding.cardTeslaFicha);
    }

    private void setupVoiceListeners() {
        binding.btnTitanVoice.setOnClickListener(v -> {
            if (getContext() == null) return;
            String mensaje = "¡Hola, futuro científico! Soy Titan, el gato astronauta explorador. " +
                    "Me encanta la física de cohetes, los volcanes de espuma y las galaxias. " +
                    "¡Acompáñame a investigar el universo!";
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTitan(mensaje);
        });

        binding.btnTeslaVoice.setOnClickListener(v -> {
            if (getContext() == null) return;
            String mensaje = "¡Kamisaraki! Soy Tesla, la duende de los Andes. " +
                    "Combino la sabiduría ecológica de la Pachamama con la robótica y la fotocatálisis del agua. " +
                    "¡Cuidemos el planeta con la ciencia!";
            CientificoVoiceHelper.getInstance(requireContext()).hablarComoTesla(mensaje);
        });
    }

    private void setupActionListeners() {
        binding.btnPersonajesPlayGame.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new VideojuegosFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.btnPersonajesOpenLab.setOnClickListener(v -> {
            LaboratorioShakeBottomSheet labModal = new LaboratorioShakeBottomSheet();
            labModal.show(getChildFragmentManager(), "LaboratorioShakeBottomSheet");
        });
    }

    @Override
    public void onPause() {
        super.onPause();
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
