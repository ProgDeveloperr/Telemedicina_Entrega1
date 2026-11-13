package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.plaf.basic.BasicButtonUI;

public final class UITheme {

	/*
	 * =========================================================
	 * PALETA PRINCIPAL - TELEMEDICINA
	 * =========================================================
	 */

	public static final Color COLOR_FONDO =
			new Color(245, 249, 251);

	public static final Color COLOR_SUPERFICIE =
			Color.WHITE;

	public static final Color COLOR_SIDEBAR =
			new Color(16, 42, 58);

	public static final Color COLOR_SIDEBAR_HOVER =
			new Color(24, 61, 82);

	public static final Color COLOR_PRIMARIO =
			new Color(11, 79, 108);

	public static final Color COLOR_ACENTO =
			new Color(1, 167, 194);

	public static final Color COLOR_ACENTO_SUAVE =
			new Color(225, 247, 250);

	public static final Color COLOR_TEXTO =
			new Color(23, 33, 43);

	public static final Color COLOR_TEXTO_SECUNDARIO =
			new Color(107, 119, 133);

	public static final Color COLOR_BORDE =
			new Color(218, 228, 234);

	public static final Color COLOR_EXITO =
			new Color(46, 157, 113);

	public static final Color COLOR_ADVERTENCIA =
			new Color(230, 162, 60);

	public static final Color COLOR_ERROR =
			new Color(217, 83, 79);

	/*
	 * =========================================================
	 * TIPOGRAFIAS
	 * =========================================================
	 */

	public static final Font FUENTE_TITULO_GRANDE =
			new Font(
					"Segoe UI",
					Font.BOLD,
					28
			);

	public static final Font FUENTE_TITULO =
			new Font(
					"Segoe UI",
					Font.BOLD,
					22
			);

	public static final Font FUENTE_SUBTITULO =
			new Font(
					"Segoe UI",
					Font.PLAIN,
					14
			);

	public static final Font FUENTE_NORMAL =
			new Font(
					"Segoe UI",
					Font.PLAIN,
					13
			);

	public static final Font FUENTE_NORMAL_NEGRITA =
			new Font(
					"Segoe UI",
					Font.BOLD,
					13
			);

	public static final Font FUENTE_BOTON =
			new Font(
					"Segoe UI",
					Font.BOLD,
					13
			);

	public static final Font FUENTE_PEQUENA =
			new Font(
					"Segoe UI",
					Font.PLAIN,
					12
			);

	/*
	 * Constructor privado.
	 *
	 * Esta clase funciona solamente como utilidad visual.
	 * No debe ser instanciada.
	 */
	private UITheme() {

	}

	/*
	 * =========================================================
	 * PANELES
	 * =========================================================
	 */

	public static JPanel crearPanelFondo() {

		JPanel panel =
				new JPanel();

		panel.setBackground(
				COLOR_FONDO
		);

		return panel;
	}

	public static JPanel crearTarjeta() {

		JPanel panel =
				new JPanel();

		panel.setBackground(
				COLOR_SUPERFICIE
		);

		panel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								COLOR_BORDE
						),
						BorderFactory.createEmptyBorder(
								18,
								18,
								18,
								18
						)
				)
		);

		return panel;
	}

	/*
	 * =========================================================
	 * LABELS
	 * =========================================================
	 */

	public static JLabel crearTitulo(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				FUENTE_TITULO
		);

		label.setForeground(
				COLOR_TEXTO
		);

		return label;
	}

	public static JLabel crearTituloGrande(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				FUENTE_TITULO_GRANDE
		);

		label.setForeground(
				COLOR_TEXTO
		);

		return label;
	}

	public static JLabel crearSubtitulo(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				FUENTE_SUBTITULO
		);

		label.setForeground(
				COLOR_TEXTO_SECUNDARIO
		);

		return label;
	}

	public static JLabel crearEtiqueta(
			String texto) {

		JLabel label =
				new JLabel(
						texto
				);

		label.setFont(
				FUENTE_NORMAL_NEGRITA
		);

		label.setForeground(
				COLOR_TEXTO
		);

		return label;
	}

	public static JLabel crearEtiquetaCentrada(
			String texto) {

		JLabel label =
				new JLabel(
						texto,
						SwingConstants.CENTER
				);

		label.setFont(
				FUENTE_NORMAL
		);

		label.setForeground(
				COLOR_TEXTO_SECUNDARIO
		);

		return label;
	}

	/*
	 * =========================================================
	 * CAMPOS
	 * =========================================================
	 */

	public static void configurarCampo(
			JTextField campo) {

		campo.setFont(
				FUENTE_NORMAL
		);

		campo.setForeground(
				COLOR_TEXTO
		);

		campo.setBackground(
				COLOR_SUPERFICIE
		);

		campo.setCaretColor(
				COLOR_PRIMARIO
		);

		campo.setPreferredSize(
				new Dimension(
						230,
						38
				)
		);

		campo.setBorder(
				crearBordeCampo()
		);
	}

	public static void configurarCampoClave(
			JPasswordField campo) {

		campo.setFont(
				FUENTE_NORMAL
		);

		campo.setForeground(
				COLOR_TEXTO
		);

		campo.setBackground(
				COLOR_SUPERFICIE
		);

		campo.setCaretColor(
				COLOR_PRIMARIO
		);

		campo.setPreferredSize(
				new Dimension(
						230,
						38
				)
		);

		campo.setBorder(
				crearBordeCampo()
		);
	}

	private static Border crearBordeCampo() {

		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(
						COLOR_BORDE
				),
				BorderFactory.createEmptyBorder(
						7,
						10,
						7,
						10
				)
		);
	}

	/*
	 * =========================================================
	 * BOTONES
	 * =========================================================
	 */

	public static void configurarBotonPrimario(
			JButton boton) {

		boton.setUI(
				new BasicButtonUI()
		);

		boton.setFont(
				FUENTE_BOTON
		);

		boton.setForeground(
				Color.WHITE
		);

		boton.setBackground(
				COLOR_PRIMARIO
		);

		boton.setFocusPainted(
				false
		);

		boton.setBorderPainted(
				false
		);

		boton.setContentAreaFilled(
				true
		);

		boton.setOpaque(
				true
		);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		boton.setPreferredSize(
				new Dimension(
						180,
						42
				)
		);

		boton.setBorder(
				BorderFactory.createEmptyBorder(
						10,
						18,
						10,
						18
				)
		);

		/*
		 * Efecto hover.
		 */
		boton.addMouseListener(
				new java.awt.event.MouseAdapter() {

					@Override
					public void mouseEntered(
							java.awt.event.MouseEvent e) {

						if (boton.isEnabled()) {

							boton.setBackground(
									COLOR_ACENTO
							);
						}
					}

					@Override
					public void mouseExited(
							java.awt.event.MouseEvent e) {

						if (boton.isEnabled()) {

							boton.setBackground(
									COLOR_PRIMARIO
							);
						}
					}
				}
		);
	}

	public static void configurarBotonSecundario(
			JButton boton) {

		boton.setFont(
				FUENTE_BOTON
		);

		boton.setForeground(
				COLOR_PRIMARIO
		);

		boton.setBackground(
				COLOR_SUPERFICIE
		);

		boton.setFocusPainted(
				false
		);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		boton.setPreferredSize(
				new Dimension(
						180,
						40
				)
		);

		boton.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								COLOR_BORDE
						),
						BorderFactory.createEmptyBorder(
								8,
								16,
								8,
								16
						)
				)
		);

		boton.setOpaque(
				true
		);
	}

	public static void configurarBotonPeligro(
			JButton boton) {

		boton.setFont(
				FUENTE_BOTON
		);

		boton.setForeground(
				Color.WHITE
		);

		boton.setBackground(
				COLOR_ERROR
		);

		boton.setFocusPainted(
				false
		);

		boton.setCursor(
				Cursor.getPredefinedCursor(
						Cursor.HAND_CURSOR
				)
		);

		boton.setPreferredSize(
				new Dimension(
						180,
						40
				)
		);

		boton.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(
								COLOR_ERROR
						),
						BorderFactory.createEmptyBorder(
								8,
								16,
								8,
								16
						)
				)
		);

		boton.setOpaque(
				true
		);
	}

	/*
	 * =========================================================
	 * TABLAS
	 * =========================================================
	 */

	public static void configurarTabla(
			JTable tabla) {

		tabla.setFont(
				FUENTE_NORMAL
		);

		tabla.setForeground(
				COLOR_TEXTO
		);

		tabla.setBackground(
				COLOR_SUPERFICIE
		);

		tabla.setSelectionBackground(
				COLOR_ACENTO_SUAVE
		);

		tabla.setSelectionForeground(
				COLOR_TEXTO
		);

		tabla.setGridColor(
				COLOR_BORDE
		);

		tabla.setRowHeight(
				32
		);

		tabla.setShowVerticalLines(
				false
		);

		tabla.setFillsViewportHeight(
				true
		);

		JTableHeader header =
				tabla.getTableHeader();

		header.setFont(
				FUENTE_NORMAL_NEGRITA
		);

		header.setForeground(
				COLOR_TEXTO
		);

		header.setBackground(
				new Color(
						236,
						243,
						247
				)
		);

		header.setPreferredSize(
				new Dimension(
						header.getPreferredSize().width,
						36
				)
		);
	}

	public static void configurarScroll(
			JScrollPane scroll) {

		scroll.setBorder(
				BorderFactory.createLineBorder(
						COLOR_BORDE
				)
		);

		scroll.getViewport()
				.setBackground(
						COLOR_SUPERFICIE
				);
	}

	/*
	 * =========================================================
	 * UTILIDADES
	 * =========================================================
	 */

	public static Border crearSeparadorInferior() {

		return BorderFactory.createMatteBorder(
				0,
				0,
				1,
				0,
				COLOR_BORDE
		);
	}

	public static Color getColorFondo() {

		return COLOR_FONDO;
	}
}