package com.launcher.samiboxtv

import android.accessibilityservice.AccessibilityService
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent

/**
 * Servicio de accesibilidad que captura teclas globalmente
 * (funciona aunque otra app esté en primer plano).
 *
 * Al detectar la tecla MENU (82), abre o cierra el System Info Overlay
 * usando el OverlayWindowManager compartido.
 */
class SamiBoxAccessibilityService : AccessibilityService() {

    private lateinit var overlayManager: OverlayWindowManager

    override fun onServiceConnected() {
        overlayManager = OverlayWindowManager(applicationContext)
        // Notificar a MainActivity que el servicio está activo
        instance = this
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                // Tecla MENU (82) → toggle overlay desde cualquier app
                KeyEvent.KEYCODE_MENU -> {
                    if (overlayManager.canDrawOverlays()) {
                        overlayManager.toggle()
                    }
                    return true // consumir el evento
                }
            }
        }
        return false // dejar pasar el resto de teclas normalmente
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) { /* no usado */ }
    override fun onInterrupt() {
        overlayManager.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayManager.hide()
        instance = null
    }

    companion object {
        var instance: SamiBoxAccessibilityService? = null

        fun isRunning() = instance != null
    }
}
