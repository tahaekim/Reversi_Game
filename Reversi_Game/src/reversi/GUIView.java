package reversi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIView implements IView {
    private IModel model;
    private IController controller;
    private JFrame player1Frame;
    private JFrame player2Frame;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JButton player1GreedyAIButton;
    private JButton player2GreedyAIButton;
    private JButton player1RestartButton;
    private JButton player2RestartButton;
    private JLabel label1;
    private JLabel label2;
    
    private int[] currentSquare;

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;
        
        player1Frame = new JFrame("Player 1");
        player1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player1Frame.setLayout(new BorderLayout());

        player1Panel = new JPanel();
        player1Panel.setLayout(new GridLayout(model.getBoardWidth(), model.getBoardHeight()));
        addBoardSquares(player1Panel);

        JPanel player1ButtonsPanel = new JPanel();
        player1ButtonsPanel.setLayout(new BoxLayout(player1ButtonsPanel, BoxLayout.Y_AXIS));

        player1GreedyAIButton = new JButton("Greedy AI");
        player1RestartButton = new JButton("Restart");

        int boardWidth = player1Panel.getPreferredSize().width;
        player1GreedyAIButton.setMaximumSize(new Dimension(boardWidth, player1GreedyAIButton.getPreferredSize().height));
        player1RestartButton.setMaximumSize(new Dimension(boardWidth, player1RestartButton.getPreferredSize().height));

        player1GreedyAIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(1);
            }
        });

        player1RestartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startup();
            }
        });

        player1ButtonsPanel.add(player1GreedyAIButton);
        player1ButtonsPanel.add(player1RestartButton);

        player1Frame.add(player1Panel, BorderLayout.CENTER);
        player1Frame.add(player1ButtonsPanel, BorderLayout.SOUTH);
        
        label1 = new JLabel();
	    label1.setHorizontalAlignment(JLabel.CENTER);
	    label1.setVerticalAlignment(JLabel.CENTER);
	    player1Frame.add(label1, BorderLayout.NORTH);

        player1Frame.setSize(400, 500);
        player1Frame.setVisible(true);
        

        player2Frame = new JFrame("Player 2");
        player2Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        player2Frame.setLayout(new BorderLayout());

        player2Panel = new JPanel();
        player2Panel.setLayout(new GridLayout(model.getBoardWidth(), model.getBoardHeight()));
        addReverseBoardSquares(player2Panel);

        JPanel player2ButtonsPanel = new JPanel();
        player2ButtonsPanel.setLayout(new BoxLayout(player2ButtonsPanel, BoxLayout.Y_AXIS));

        player2GreedyAIButton = new JButton("Greedy AI");
        player2RestartButton = new JButton("Restart");

        boardWidth = player2Panel.getPreferredSize().width;
        player2GreedyAIButton.setMaximumSize(new Dimension(boardWidth, player2GreedyAIButton.getPreferredSize().height));
        player2RestartButton.setMaximumSize(new Dimension(boardWidth, player2RestartButton.getPreferredSize().height));

        player2GreedyAIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.doAutomatedMove(2);
            }
        });

        player2RestartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.startup();
            }
        });

        player2ButtonsPanel.add(player2GreedyAIButton);
        player2ButtonsPanel.add(player2RestartButton);

        player2Frame.add(player2Panel, BorderLayout.CENTER);
        player2Frame.add(player2ButtonsPanel, BorderLayout.SOUTH);
        
        label2 = new JLabel();
	    label2.setHorizontalAlignment(JLabel.CENTER);
	    label2.setVerticalAlignment(JLabel.CENTER);
	    player2Frame.add(label2, BorderLayout.NORTH);

        player2Frame.setSize(400, 500);
        player2Frame.setVisible(true);
        
    }

    public void refreshView() {
        int boardWidth = model.getBoardWidth();
        int boardHeight = model.getBoardHeight();

        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                GUIViewBoardSquare square = (GUIViewBoardSquare) player1Panel.getComponent(row * boardWidth + col);
                int content = model.getBoardContents(row, col);
                square.setContent(content);
                square.setSelected(currentSquare != null);
            }
        }

        for (int row = boardHeight - 1; row >= 0; row--) {
            for (int col = boardWidth - 1; col >= 0; col--) {
                GUIViewBoardSquare square = (GUIViewBoardSquare) player2Panel.getComponent((boardHeight - 1 - row) * boardWidth + (boardWidth - 1 - col));
                int content = model.getBoardContents(row, col);
                square.setContent(content);
                square.setSelected(currentSquare != null);
            }
        }
    }


	@Override
	public void feedbackToUser(int player, String message) {
	    if (player == 1) {
	        label1.setText(message);
	    } else {
	    	label2.setText(message);
	    }
	}


	private void addBoardSquares(JPanel panel) {
        currentSquare = new int[2];
        int boardWidth = model.getBoardWidth();
        int boardHeight = model.getBoardHeight();

        for (int row = 0; row < boardHeight; row++) {
            final int finalRow = row;
            for (int col = 0; col < boardWidth; col++) {
                final int finalCol = col;
                GUIViewBoardSquare square = new GUIViewBoardSquare();

                square.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.squareSelected(1, finalRow, finalCol);
                        currentSquare[0] = finalRow;
                        currentSquare[1] = finalCol;
                    }
                });

                panel.add(square);
            }
        }
    }

    private void addReverseBoardSquares(JPanel panel) {
        currentSquare = new int[2];
        int boardWidth = model.getBoardWidth();
        int boardHeight = model.getBoardHeight();

        for (int row = boardHeight - 1; row >= 0; row--) {
            final int finalRow = row;
            for (int col = boardWidth - 1; col >= 0; col--) {
                final int finalCol = col;
                GUIViewBoardSquare square = new GUIViewBoardSquare();

                square.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.squareSelected(2, finalRow, finalCol);
                        currentSquare[0] = finalRow;
                        currentSquare[1] = finalCol;
                    }
                });

                panel.add(square);
            }
        }
    }

}
