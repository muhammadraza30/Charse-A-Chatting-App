package client;

import interfaces.ChatEvent;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import custom_components.ChatBox;
import custom_components.ModelMessage;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import main.User;
import main.UserFileHandling;
import queue.Message;
import queue.MessageFileHandling;
import queue.Queue;
import server.MessageHandler;

public class Menu extends javax.swing.JFrame {

    /**
     * Creates new form Menu
     */
    UserFileHandling h = new UserFileHandling();
    ArrayList<User> arrlist = h.getUsers();
    private final Client client;
    private MessageFileHandling handeling = new MessageFileHandling();
    private Map<String, String> userImages = new HashMap<>();

    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy, hh:mmaa");
    Socket s;
    Queue q;
    String image;

    public Menu() {
        initComponents();
        for (User user : arrlist) {
            userImages.put(user.getUserName(), user.getImagePath());
        }
        q = new Queue();
        this.client = Client.getInstance();
        s = this.client.getSocket();
        setIconImage(new ImageIcon(getClass().getResource("/images/charseicon.png")).getImage());
        searchBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 60));
        messageButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/messenger.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        groupButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/groups.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        statusButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/status.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        openChat.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/open.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        settingButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/setting.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        logoutButton.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/images/logout.png")).getImage().getScaledInstance(messageButton.getPreferredSize().width, messageButton.getPreferredSize().height, Image.SCALE_AREA_AVERAGING)));
        jTable1.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 24));
        TableCellRenderer rendererFromHeader = jTable1.getTableHeader().getDefaultRenderer();
        JLabel headerLabel = (JLabel) rendererFromHeader;
        JTableHeader header = jTable1.getTableHeader();
        header.setBackground(Color.decode("#434343"));
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setVerticalAlignment(JLabel.CENTER);
        jTable1.setAlignmentX(CENTER_ALIGNMENT);
        jTable1.setAlignmentY(CENTER_ALIGNMENT);
        jTable1.getAlignmentX();
        jTable1.getAlignmentY();
        jTable1.getTableHeader().setBackground(new Color(242, 242, 242));
        jTable1.getTableHeader().setForeground(new Color(0, 0, 0));
        jTable2.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 24));
        TableCellRenderer rendererFromHeader1 = jTable2.getTableHeader().getDefaultRenderer();
        JLabel headerLabel1 = (JLabel) rendererFromHeader1;
        JTableHeader header1 = jTable2.getTableHeader();
        header1.setBackground(Color.decode("#434343"));
        headerLabel1.setHorizontalAlignment(JLabel.CENTER);
        headerLabel1.setVerticalAlignment(JLabel.CENTER);
        jTable2.setAlignmentX(CENTER_ALIGNMENT);
        jTable2.setAlignmentY(CENTER_ALIGNMENT);
        jTable2.getAlignmentX();
        jTable2.getAlignmentY();
        jTable2.getTableHeader().setBackground(new Color(242, 242, 242));
        jTable2.getTableHeader().setForeground(new Color(0, 0, 0));

        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"User", "Status"}) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTable1.setModel(model);
        for (ClientStatus client : MessageHandler.getAllClients(PanelLoginAndRegister.userN)) {
            Object[] rowData = {client.getUser().getUserName(), client.getStatus()};
            model.addRow(rowData);
//            username = client.getUser().getUserName();
        }
        for (int i = 0; i < arrlist.size(); i++) {
            if (arrlist.get(i).getUserName().equals(PanelLoginAndRegister.userN)) {
                image = arrlist.get(i).getImagePath();
                break;
            }
        }

        new Thread(new ClientHandler(s, chatArea)).start();
        chatArea.setTitle("");
        chatArea.setVisible(false);
        chatArea.setBackground(new Color(37, 81, 149));
        chatArea.addChatEvent(new ChatEvent() {
            @Override
            public void mousePressedSendButton(ActionEvent evt) {

                Icon icon = new ImageIcon(getClass().getResource(image));

                String date = df.format(new Date());
                String message = chatArea.getText().trim();
                if (!message.isEmpty()) {
                    try {
                        client.sendMessage(message);

                    } catch (IOException ex) {
                        Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (tabbedPaneUI1.getSelectedIndex() == 1) {
                        q.enQueue(message);
                    } else {
                        try {
                            new MessageFileHandling().storeMessages((String) jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0), message);
                            new MessageFileHandling().getMessages(PanelLoginAndRegister.userN, message);

                        } catch (IOException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    chatArea.addChatBox(new ModelMessage(icon, "You", date, message), ChatBox.BoxType.RIGHT);
                    chatArea.clearTextAndGrabFocus();
                }

            }

            @Override
            public void mousePressedFileButton(ActionEvent evt) {

            }

            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER && evt.isShiftDown()) {

                    Icon icon = new ImageIcon(getClass().getResource(image));

                    String date = df.format(new Date());
                    String message = chatArea.getText().trim();
                    if (!message.isEmpty()) {
                        try {
                            client.sendMessage(message);

                        } catch (IOException ex) {
                            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (tabbedPaneUI1.getSelectedIndex() == 1) {
                            q.enQueue(message);
                        } else {
                            try {
                                new MessageFileHandling().storeMessages((String) jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0), message);
                                new MessageFileHandling().getMessages(PanelLoginAndRegister.userN, message);

                            } catch (IOException ex) {
                                Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        chatArea.addChatBox(new ModelMessage(icon, "You", date, message), ChatBox.BoxType.RIGHT);
                        chatArea.clearTextAndGrabFocus();
                    }
                }
            }
        });
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable1);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable1);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable1);
            }
        });
    }

    private void searchContacts(String searchTerm, JTable jTable) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.setRowCount(0);

        if (searchTerm == null || searchTerm.isEmpty()) {
            for (ClientStatus client : MessageHandler.getAllClients(PanelLoginAndRegister.userN)) {
                Object[] rowData = {client.getUser().getUserName(), client.getStatus()};
                model.addRow(rowData);
            }
        } else {
            for (ClientStatus client : MessageHandler.getAllClients(PanelLoginAndRegister.userN)) {
                if (client.getUser().getUserName().toLowerCase().contains(searchTerm.toLowerCase())) {
                    Object[] rowData = {client.getUser().getUserName(), client.getStatus()};
                    model.addRow(rowData);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        polygonCorner1 = new custom_components.PolygonCorner();
        jPanel6 = new javax.swing.JPanel();
        searchBar = new custom_components.MyTextField();
        jPanel7 = new javax.swing.JPanel();
        openChat = new custom_components.Button();
        messageButton = new custom_components.Button();
        groupButton = new custom_components.Button();
        statusButton = new custom_components.Button();
        logoutButton = new custom_components.Button();
        tabbedPaneUI1 = new custom_components.TabbedPaneUI();
        scrollPaneUI1 = new custom_components.ScrollPaneUI();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        scrollPaneUI2 = new custom_components.ScrollPaneUI();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        scrollPaneUI3 = new custom_components.ScrollPaneUI();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        settingButton = new custom_components.Button();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chatArea = new custom_components.ChatArea();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        darkMode = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Charse");
        setBackground(new java.awt.Color(243, 243, 243));
        setResizable(false);

        jPanel6.setBackground(new java.awt.Color(230, 230, 230));

        searchBar.setBackground(new java.awt.Color(253, 253, 253));
        searchBar.setBorder(null);
        searchBar.setForeground(new java.awt.Color(51, 51, 51));
        searchBar.setFont(new java.awt.Font("Georgia", 0, 16)); // NOI18N
        searchBar.setHint("Search Contact");
        searchBar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBarActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(230, 230, 230));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        openChat.setBackground(new java.awt.Color(230, 230, 230));
        openChat.setBorder(null);
        openChat.setPreferredSize(new java.awt.Dimension(20, 20));
        openChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openChatActionPerformed(evt);
            }
        });
        jPanel7.add(openChat, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 40, 30));

        messageButton.setBackground(new java.awt.Color(230, 230, 230));
        messageButton.setForeground(new java.awt.Color(204, 204, 204));
        messageButton.setPreferredSize(new java.awt.Dimension(24, 24));
        messageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageButtonActionPerformed(evt);
            }
        });
        jPanel7.add(messageButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 30));

        groupButton.setBackground(new java.awt.Color(230, 230, 230));
        groupButton.setBorder(null);
        groupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                groupButtonActionPerformed(evt);
            }
        });
        jPanel7.add(groupButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 30, 30));

        statusButton.setBackground(new java.awt.Color(230, 230, 230));
        statusButton.setBorder(null);
        statusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusButtonActionPerformed(evt);
            }
        });
        jPanel7.add(statusButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 30, 30));

        logoutButton.setBackground(new java.awt.Color(230, 230, 230));
        logoutButton.setBorder(null);
        logoutButton.setToolTipText("Logout");
        logoutButton.setPreferredSize(new java.awt.Dimension(20, 20));
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });
        jPanel7.add(logoutButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, 30, 30));

        tabbedPaneUI1.setBackground(new java.awt.Color(230, 230, 230));
        tabbedPaneUI1.setForeground(new java.awt.Color(255, 255, 255));
        tabbedPaneUI1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        scrollPaneUI1.setBackground(new java.awt.Color(230, 230, 230));
        scrollPaneUI1.setBorder(null);
        scrollPaneUI1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel4.setBackground(new java.awt.Color(254, 254, 254));

        jTable1.setBackground(new java.awt.Color(35, 186, 113));
        jTable1.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "User", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(102, 102, 102));
        jTable1.setMaximumSize(new java.awt.Dimension(2147483647, 20000));
        jTable1.setMinimumSize(new java.awt.Dimension(165, 605));
        jTable1.setPreferredSize(new java.awt.Dimension(225, 605));
        jTable1.setRowHeight(40);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(150);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );

        scrollPaneUI1.setViewportView(jPanel4);

        tabbedPaneUI1.addTab("", scrollPaneUI1);

        scrollPaneUI2.setBackground(new java.awt.Color(230, 230, 230));
        scrollPaneUI2.setBorder(null);
        scrollPaneUI2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel5.setBackground(new java.awt.Color(254, 254, 254));

        jTable2.setBackground(new java.awt.Color(35, 186, 113));
        jTable2.setFont(new java.awt.Font("Book Antiqua", 0, 18)); // NOI18N
        jTable2.setForeground(new java.awt.Color(255, 255, 255));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "User", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTable2.setGridColor(new java.awt.Color(102, 102, 102));
        jTable2.setMaximumSize(new java.awt.Dimension(2147483647, 20000));
        jTable2.setMinimumSize(new java.awt.Dimension(165, 605));
        jTable2.setPreferredSize(new java.awt.Dimension(225, 605));
        jTable2.setRowHeight(40);
        jTable2.setSelectionBackground(new java.awt.Color(35, 186, 113));
        jTable2.setSelectionForeground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(150);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(150);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 184, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 178, Short.MAX_VALUE))
        );

        scrollPaneUI2.setViewportView(jPanel5);

        tabbedPaneUI1.addTab("", scrollPaneUI2);

        scrollPaneUI3.setBackground(new java.awt.Color(230, 230, 230));
        scrollPaneUI3.setBorder(null);
        scrollPaneUI3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanel8.setBackground(new java.awt.Color(254, 254, 254));

        jLabel3.setFont(new java.awt.Font("Georgia", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("STATUS");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addContainerGap(128, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(532, Short.MAX_VALUE))
        );

        scrollPaneUI3.setViewportView(jPanel8);

        tabbedPaneUI1.addTab("", scrollPaneUI3);

        jPanel7.add(tabbedPaneUI1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 300, 610));

        settingButton.setBackground(new java.awt.Color(230, 230, 230));
        settingButton.setBorder(null);
        settingButton.setPreferredSize(new java.awt.Dimension(20, 20));
        settingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingButtonActionPerformed(evt);
            }
        });
        jPanel7.add(settingButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 40, 40));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchBar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(154, 154, 154))
        );

        jLabel1.setFont(new java.awt.Font("Imprint MT Shadow", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(35, 186, 113));
        jLabel1.setText("CHARSE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        chatArea.setVerifyInputWhenFocusTarget(false);

        jMenu3.setText("File");

        darkMode.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_DOWN_MASK));
        darkMode.setText("Exit");
        darkMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                darkModeActionPerformed(evt);
            }
        });
        jMenu3.add(darkMode);

        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(chatArea, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chatArea, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void darkModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_darkModeActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_darkModeActionPerformed

    private void searchBarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBarActionPerformed
        // TODO add your handling code 
    }//GEN-LAST:event_searchBarActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed

        try {
            // TODO add your handling code here:
            if (this.client.isConnected()) {
                this.client.disconenct();
            }
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
        setVisible(false);
//        new Menu().dispose();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void statusButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusButtonActionPerformed
        // TODO add your handling code here:
        tabbedPaneUI1.setSelectedIndex(2);
        chatArea.setVisible(false);
        chatArea.clearChatBox();
    }//GEN-LAST:event_statusButtonActionPerformed

    private void groupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_groupButtonActionPerformed
        // TODO add your handling code here:
        chatArea.setTitle(capitalizeWord("group chat"));
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, new String[]{"User", "Status"}) {
            Class[] types = new Class[]{
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };
        jTable2.setModel(model);
        for (ClientStatus client : MessageHandler.getAllClients(PanelLoginAndRegister.userN)) {
            Object[] rowData = {client.getUser().getUserName(), client.getStatus()};
            model.addRow(rowData);
        }
        searchBar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable2);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable2);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchContacts(searchBar.getText(), jTable2);
            }
        });
        tabbedPaneUI1.setSelectedIndex(1);
        chatArea.setVisible(true);
        chatArea.clearChatBox();

        ArrayList<Message> messages = handeling.getMessage();
        for (Message message : messages) {
            String date = df.format(new Date(message.getTimestamp())); // use the stored timestamp
            String senderUsername = message.getSenderUsername();
            String messageText = message.getMessages();

            // Get the image path for the sender
            String imagePath = userImages.get(senderUsername);

            // Check if the imagePath is not null
            if (imagePath != null) {
                // Create an icon from the image path
                Icon icon = new ImageIcon(getClass().getResource(imagePath));

                // Add the chat box to the chat area
                if (senderUsername.equals(PanelLoginAndRegister.userN)) {
                    chatArea.addChatBox(new ModelMessage(icon, "You", date, messageText), ChatBox.BoxType.RIGHT);
                } else {
                    chatArea.addChatBox(new ModelMessage(icon, senderUsername, date, messageText), ChatBox.BoxType.LEFT);
                }
            }
        }
        chatArea.clearTextAndGrabFocus();


    }//GEN-LAST:event_groupButtonActionPerformed

    private void messageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageButtonActionPerformed
        // TODO add your handling code here:
        tabbedPaneUI1.setSelectedIndex(0);
    }//GEN-LAST:event_messageButtonActionPerformed

    private void openChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openChatActionPerformed
        // TODO add your handling code here
        tabbedPaneUI1.setSelectedIndex(0);
        int row = jTable1.getSelectedRow();
        if (row >= 0) {
            String user = (String) jTable1.getModel().getValueAt(row, 0);
            UserFileHandling h = new UserFileHandling();
            for (User users : h.getUsers()) {
                if (users.getUserName().equals(user)) {
                    chatArea.setTitle(capitalizeWord(users.getFullName()));
                    break;
                }
            }
            chatArea.setVisible(true);
            chatArea.clearChatBox();

            ArrayList<Message> messages = handeling.getMessages(this.chatArea.loggedInUser, user);
            for (Message message : messages) {
                String date = df.format(new Date(message.getTimestamp())); // use the stored timestamp
                String senderUsername = message.getSenderUsername();
                String messageText = message.getMessages();

                // Get the image path for the sender
                String imagePath = userImages.get(senderUsername);

                // Check if the imagePath is not null
                if (imagePath != null) {
                    // Create an icon from the image path
                    Icon icon = new ImageIcon(getClass().getResource(imagePath));

                    // Add the chat box to the chat area
                    if (senderUsername.equals(PanelLoginAndRegister.userN)) {
                        chatArea.addChatBox(new ModelMessage(icon, "You", date, messageText), ChatBox.BoxType.RIGHT);
                    } else {
                        chatArea.addChatBox(new ModelMessage(icon, senderUsername, date, messageText), ChatBox.BoxType.LEFT);
                    }
                }
            }
            chatArea.clearTextAndGrabFocus();

        } else {
            // Display a message or take appropriate action when no row is selected
            JOptionPane.showMessageDialog(this, "Please select a user to start a private chat.", "No User Selected", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_openChatActionPerformed

    private void settingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingButtonActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Work in Progress", "Setting Info", 1);
    }//GEN-LAST:event_settingButtonActionPerformed

    public static String capitalizeWord(String str) {
        String words[] = str.split("\\s");
        String capitalizeWord = "";
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

//        /* Create and display the form */
//        FlatLaf.registerCustomDefaultsSource("style");
//        FlatIntelliJLaf.setup();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private custom_components.ChatArea chatArea;
    private javax.swing.JMenuItem darkMode;
    private custom_components.Button groupButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private custom_components.Button logoutButton;
    private custom_components.Button messageButton;
    private custom_components.Button openChat;
    private custom_components.PolygonCorner polygonCorner1;
    private custom_components.ScrollPaneUI scrollPaneUI1;
    private custom_components.ScrollPaneUI scrollPaneUI2;
    private custom_components.ScrollPaneUI scrollPaneUI3;
    private custom_components.MyTextField searchBar;
    private custom_components.Button settingButton;
    private custom_components.Button statusButton;
    private custom_components.TabbedPaneUI tabbedPaneUI1;
    // End of variables declaration//GEN-END:variables

}
