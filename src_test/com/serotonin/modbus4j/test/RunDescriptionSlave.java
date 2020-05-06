package com.serotonin.modbus4j.test;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Color;

public class RunDescriptionSlave {

	private JFrame frmModbusDescriptionDemonstration;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunDescriptionSlave window = new RunDescriptionSlave();
					window.frmModbusDescriptionDemonstration.setVisible(true);
					ListenerTest3.main(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RunDescriptionSlave() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmModbusDescriptionDemonstration = new JFrame();
		frmModbusDescriptionDemonstration.setTitle("Modbus description demonstration");
		frmModbusDescriptionDemonstration.setBounds(100, 100, 453, 85);
		frmModbusDescriptionDemonstration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmModbusDescriptionDemonstration.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		
		JLabel lblModbusDescriptionSlave = new JLabel("Modbus description slave server: ");
		lblModbusDescriptionSlave.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmModbusDescriptionDemonstration.getContentPane().add(lblModbusDescriptionSlave);
		
		JLabel lblRunning = new JLabel("Running");
		lblRunning.setForeground(Color.BLUE);
		lblRunning.setFont(new Font("Tahoma", Font.BOLD, 18));
		frmModbusDescriptionDemonstration.getContentPane().add(lblRunning);
	}

}
