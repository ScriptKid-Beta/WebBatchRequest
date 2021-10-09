package com.webRequest;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gui extends JFrame {
    private JTextField uaText,dataText, targetsPathText;
    private JMenuBar fileMenu,helpMenu,proxyMenu;
    private JMenuItem importTargetsMenuItem,exportMenuItem,aboutMenuItem,httpMenuItem;
    private JPanel topPanel, westPanel, eastPanel;
    private JLabel targetsLabel, pathLabel,cookiesLabel,uaLabel,dataLabel,headLabel,countLabel;
    private JButton runButton,stopButton,clearButton;
    private JComboBox threadBox,enctypeBox;
    private JTextArea targetsTextArea,headTextArea,cookiesTextArea;
    private JScrollPane targetsScroll,tableScroll;
    private JTable table;
    private Vector<String> columnNames;
    private DefaultTableModel dataModel;
    private ButtonGroup requestsButtonGroup;
    private JRadioButton getRadio,postRadio,headRadio;
    private JCheckBox followBox;
    private ExecutorService pool;
    private int total,doneCount;
    private JPopupMenu rightMenu;
    private String targetUrl,host,portString;
    private int port;
    private boolean proxyPower;
    private JMenu file,help,proxy;
    private HashMap<String, String> bodyData;

    public Gui()
        {
            JFrame.setDefaultLookAndFeelDecorated(true);
            this.setTitle("WEB批量请求器");    //设置显示窗口标题
            this.setBounds(400,300,1210,720);
            // 屏幕中央
            setLocationRelativeTo(null);


            // 头部面板内容
            fileMenu = new JMenuBar();
            file = fileMenu.add(new JMenu("文件"));
            importTargetsMenuItem = new JMenuItem("导入目标");
            exportMenuItem = new JMenuItem("导出结果");
            file.add(importTargetsMenuItem);
            file.add(exportMenuItem);
            helpMenu=new JMenuBar();

            help = helpMenu.add(new JMenu("帮助"));
            aboutMenuItem = new JMenuItem("关于");
            help.add(aboutMenuItem);

            proxyMenu=new JMenuBar();
            proxy = proxyMenu.add(new JMenu("代理设置"));
            httpMenuItem = new JMenuItem("添加HTTP代理");
            proxy.add(httpMenuItem);

            // 头部面板
            topPanel = new JPanel();
            topPanel.add(fileMenu);
            topPanel.add(proxyMenu);
            topPanel.add(helpMenu);
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));



            // 左部面板（西）
            westPanel = new JPanel();
//            GridBagLayout gridBagLayout=new GridBagLayout();
//            GridBagConstraints gridBagConstraints=new GridBagConstraints();
//            gridBagConstraints.fill=GridBagConstraints.BOTH;
            // 流式布局
            westPanel.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));

            // 左部面板组件
            targetsLabel = new JLabel("targets:                                          ");
            pathLabel = new JLabel("/");
            headLabel = new JLabel("自定义Header:");
            cookiesLabel = new JLabel("自定义Cookies:");
            uaLabel = new JLabel("自定义User-Agent:");

            runButton = new JButton("Run");
            runButton.setEnabled(true);
            runButton.setBorderPainted(true);

            stopButton = new JButton("Stop");
            stopButton.setEnabled(true);
            stopButton.setBorderPainted(true);

            clearButton = new JButton("Clear");
            clearButton.setEnabled(true);
            clearButton.setBorderPainted(true);

            countLabel = new JLabel("0/5");

            threadBox = new JComboBox();
            threadBox.addItem("--请选择线程--");
            threadBox.addItem("1");
            threadBox.addItem("5");
            threadBox.addItem("25");
            threadBox.addItem("50");
            threadBox.addItem("75");
            threadBox.addItem("100");
            targetsTextArea = new JTextArea(8,40);
            targetsTextArea.addFocusListener(new DefaultTextarea("https://www.baidu.com\nhttps://www.qq.com\nhttps://www.sina.com.cn/\nhttp://weather.sina.com.cn\nhttps://www.nday.top\n",targetsTextArea));
            targetsScroll = new JScrollPane(targetsTextArea);

            targetsPathText = new JTextField(9);
            targetsPathText.addFocusListener(new DefaultText("默认为空", targetsPathText));

            headTextArea = new JTextArea(2,51);
            headTextArea.addFocusListener(new DefaultTextarea("X-Forwarded-for: 127.0.0.1\nReferer: ...",headTextArea));

            cookiesTextArea = new JTextArea(2,51);
            cookiesTextArea.addFocusListener(new DefaultTextarea("SESSION=A202106084F2...",cookiesTextArea));

            uaText = new JTextField("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36(KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36",51);
            //自动换行
            targetsTextArea.setLineWrap(true);
            cookiesTextArea.setLineWrap(true);
            headTextArea.setLineWrap(true);

            requestsButtonGroup = new ButtonGroup();
            getRadio = new JRadioButton("get", true);
            postRadio = new JRadioButton("post");
            headRadio = new JRadioButton("head");
            requestsButtonGroup.add(getRadio);
            requestsButtonGroup.add(postRadio);
            requestsButtonGroup.add(headRadio);

            followBox = new JCheckBox("是否跟随302跳转");

            dataLabel=new JLabel("post_data:");
            dataText = new JTextField("",51);

            dataText.setEditable(false);

            enctypeBox = new JComboBox();
            enctypeBox.addItem("application/x-www-form-urlencoded");
            enctypeBox.addItem("application/json");
            enctypeBox.addItem("multipart/form-data");
            enctypeBox.setEnabled (false);

            // 填充换行保持布局
//            JButton padding=new JButton("                                                     "
//                    +"                               "+"                               ");
//            padding.setContentAreaFilled(false);
//            padding.setBorderPainted(false);
//            padding.setEnabled(false);
            // 组件添加到左部面板中
            westPanel.add(targetsLabel);
            westPanel.add(targetsScroll);
            westPanel.add(pathLabel);
            westPanel.add(targetsPathText);

            westPanel.add(headLabel);
            westPanel.add(headTextArea);

            westPanel.add(cookiesLabel);
            westPanel.add(cookiesTextArea);

            westPanel.add(uaLabel);
            westPanel.add(uaText);
            westPanel.add(dataLabel);
            westPanel.add(dataText);

            westPanel.add(enctypeBox);

            westPanel.add(getRadio);
            westPanel.add(postRadio);
            westPanel.add(headRadio);

            westPanel.add(followBox);

            westPanel.add(threadBox);
            westPanel.add(runButton);
            westPanel.add(stopButton);
            westPanel.add(clearButton);
            westPanel.add(countLabel);

            // 右部面板（东）
            eastPanel = new JPanel();
            eastPanel.setLayout(new BorderLayout(0, 0));

            // 表头（列名）
            columnNames = new Vector<String>();
            columnNames.add("target");
            columnNames.add("status");
            columnNames.add("title");
            columnNames.add("banner");
            columnNames.add("contentLength");
            // 创建表格模型
            dataModel = new DefaultTableModel(columnNames, 0);
            // 创建JTable表格组件
            table = new JTable(dataModel);
            // JTable内容居中
//            DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
//            cr.setHorizontalAlignment(JLabel.CENTER);
//            table.setDefaultRenderer(Object.class, cr);
//            table.setRowHeight(30);
            // 设置列表可排序
            table.setAutoCreateRowSorter(true);
            // 设置target列的宽度
            TableColumn targetsColumn = table.getColumnModel().getColumn(0);
            targetsColumn.setPreferredWidth(130);

            // 创建带滚动条的面板，并将表格添加到带滚动条的面板中
            tableScroll = new JScrollPane(table);
            // 将表头添加到面板中（布局的上方）
            eastPanel.add(table.getTableHeader(), BorderLayout.NORTH);
            // 将带滚动条的面板添加到布局中（布局的中间）
            eastPanel.add(tableScroll, BorderLayout.CENTER);

            // 添加到JFrame
            getContentPane().add(topPanel, BorderLayout.NORTH);
            getContentPane().add(westPanel,BorderLayout.WEST);
            getContentPane().add(eastPanel, BorderLayout.EAST);
            //设置区域面板大小
            westPanel.setPreferredSize(new Dimension(588,510));
            eastPanel.setPreferredSize(new Dimension(600,510));

            //窗口显示
            setVisible(true);
            // 用户是否可随意改窗口大小
//            setResizable(false);
            // 点击x直接结束当前程序
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


// ---------------------事件处理---------------------------

            // 单选框get/post事件
            ActionListener requests_Listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    switch (e.getActionCommand()){
                        case "post":
                            dataText.setEditable(true);
                            enctypeBox.setEnabled (true);
                            break;
                        default:
                            dataText.setEditable(false);
                            enctypeBox.setEnabled (false);
                            dataText.setText("");
                            break;
                    }
                }
            };
            getRadio.addActionListener(requests_Listener);
            postRadio.addActionListener(requests_Listener);
            headRadio.addActionListener(requests_Listener);


            // run按钮点击事件
            runButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //点击后不可点击
                    runButton.setEnabled(false);
                    // 预览body的Map集合
                    bodyData = new HashMap<String, String>();

                    // 获取输入值
                    String targetsBody = targetsTextArea.getText();
                    String cookiesBody = cookiesTextArea.getText();
                    String uaBody = uaText.getText();
                    String[] str = targetsBody.split("\n");
                    String headBody = headTextArea.getText();
                    String targetsPathBody = targetsPathText.getText();
//                    String targetsBody3 = targetsBody2 == "默认为空" ? "":targetsBody2;
//                    System.out.println(targetsBody3);

                    // 数据处理 看用户是否输入的只是域名
                    ArrayList<String> str2 = new ArrayList<>();
//                    for (String target : str) {
//                        if (target.startsWith("http://") || target.startsWith("https://"))
//                        {   if (targetsPathBody.equals("默认为空")){
//                            str2.add(target);
//                        }else {
//                            str2.add(target+"/"+targetsPathBody);
//                        }
//                        }else {
//                            if (targetsPathBody.equals("默认为空")){
//                            str2.add("https://"+target);
//                            str2.add("http://"+target);
//                            } else {
//                            str2.add("https://"+target+"/"+targetsPathBody);
//                            str2.add("http://"+target+"/"+targetsPathBody);
//                            }
//                        }
//                    }
                    if (targetsPathBody.equals("默认为空")){
                    for (String target : str) {
                        if (target.startsWith("http://") || target.startsWith("https://"))
                        {
                            str2.add(target);
                        }
                        else {
                            str2.add("https://"+target);
                            str2.add("http://"+target);
                        }
                    }
                    }else {
                        for (String target : str) {
                            if (target.startsWith("http://") || target.startsWith("https://"))
                            {
                                str2.add(target+"/"+targetsPathBody);
                            }
                            else {
                                str2.add("https://"+target+"/"+targetsPathBody);
                                str2.add("http://"+target+"/"+targetsPathBody);
                            }
                        } }
                    // 多少个目标
                    total = str2.size();

                    // 判断用什么方式请求
                    if (postRadio.isSelected()){
                        String dataBody = dataText.getText();
                        Object enctypeBody = enctypeBox.getSelectedItem();
                        httpData(str2,cookiesBody,uaBody,headBody,"post",dataBody,enctypeBody.toString());
                    }
                    if (headRadio.isSelected()){
                        httpData(str2,cookiesBody,uaBody,headBody,"head",null,null);
                    }
                    if (getRadio.isSelected()){
                        httpData(str2,cookiesBody,uaBody,headBody,"get",null,null);
                    }

                    //重置完成数以列表内容
                    doneCount = 0;
                    dataModel.setRowCount(0);
                }
            });

            // stop 按钮事件
            stopButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pool.shutdownNow();
                    runButton.setEnabled(true);
                }
            });

            // clear 按钮事件
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dataModel.setRowCount(0);
                    countLabel.setText(0+"/"+total);
                }
            });

            // 导入文件事件
            importTargetsMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent  e) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    String ext[] = {"txt"};
                    FileNameExtensionFilter extFilter = new FileNameExtensionFilter("txt", ext);
                    fileChooser.setFileFilter(extFilter); // 设置文件过滤器
                    fileChooser.showOpenDialog(null);
                    File selectFile = fileChooser.getSelectedFile();
                    if (selectFile != null) {
                        // 获取文件路径
                        String filepath = selectFile.getPath().trim();
                        // 调用文件读取TextFile
                        List<String> strings = TxtFile.readTxtFile(filepath);
                        if (strings != null && !strings.isEmpty()){
                            total = strings.size();
                            countLabel.setText("0/"+total);
                            targetsTextArea.setText("");
                            for (String string : strings) {
                                targetsTextArea.append(string+"\n");}
                        }else {
                            // 警告框
                            JOptionPane.showMessageDialog(null, "选择文件有误,请选择txt文件导入", "error",JOptionPane.WARNING_MESSAGE);
//                            targetsTextArea.setText("#error#选择文件有误,请选择txt文件导入");
                        }
                    }
                }
            });

            // 导出结果事件
            exportMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent  e) {
                    JFileChooser fileChooser = new JFileChooser();
                    // 设置默认导出文件名
                    String defaultFileName = "results.csv";
                    fileChooser.setDialogTitle("保存文件");

                    fileChooser.setSelectedFile(new File(defaultFileName));
                    int saveDialog = fileChooser.showDialog(null, "保存文件");

                    // 判断用户是否点击保存文件
                    if (saveDialog == fileChooser.APPROVE_OPTION) {
                        // 获取文件名
                        String filename = fileChooser.getSelectedFile().toString();
                        // 判断文件是否为csv结尾
                        if (!filename.endsWith(".csv")) {
                            filename = filename + ".csv";
                        }
                        File file = new File(filename);
                        // 判断文件是否存在
                        if (file.exists()) {
                            int i = JOptionPane.showConfirmDialog(null, "该文件已经存在，确定要覆盖吗？");
                            if (i != JOptionPane.YES_OPTION) {
                                return;
                            }
                        }
                        try {
                            // 调用exportTable方法写入csv
                            exportTable(table, file);
                        } catch (IOException ex) {
//                            System.out.println(ex.getMessage());
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, ex.getMessage(), "error",JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            });

            // 关于事件
            aboutMenuItem.addActionListener(new about());

            // 代理设置事件
            httpMenuItem.addActionListener(new proxy());

            // table事件 右键事件
            table.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {

                    rightMenu = new JPopupMenu();
                    // 复制目标地址
                    JMenuItem copyMenItem = new JMenuItem();
                    copyMenItem.setText("  复制目标地址  ");
                    //设置快捷键
//                    copyMenItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
                    copyMenItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            // 获取系统剪切板
                            Clipboard clipboard=Toolkit.getDefaultToolkit().getSystemClipboard();
                            // 构建String类型
                            StringSelection selection = new StringSelection(targetUrl);
                            // 添加文本到系统剪切板
                            clipboard.setContents(selection,selection);
                        }
                    });

                    // 使用默认浏览器打开
                    JMenuItem browserMenItem = new JMenuItem();
                    browserMenItem.setText("  使用默认浏览器打开  ");
                    browserMenItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            //该操作需要做的事
                            try {
                                java.net.URI uri = java.net.URI.create(targetUrl);
                                // 获取当前系统桌面扩展
                                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                                // 判断系统桌面是否支持要执行的功能
                                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                                    dp.browse(uri);
                                    // 获取系统默认浏览器打开链接
                                }
                            } catch (java.lang.NullPointerException e1) {
                                // 此为uri为空时抛出异常
                                e1.printStackTrace();
                            } catch (java.io.IOException e1) {
                                // 此为无法获取系统默认浏览器
                                e1.printStackTrace();
                            }
                        }
                    });

                    // 预览Body
                    JMenuItem viewMenItem = new JMenuItem();
                    viewMenItem.setText("  预览Body  ");
                    viewMenItem.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                            // 新建一个frame
                            JDialog viewBodyFrame = new JDialog();
                            viewBodyFrame.setBounds(795,350,500,500);
                            viewBodyFrame.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
                            // 设置未关闭该窗口无法操作其他窗口
                            viewBodyFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                            viewBodyFrame.setTitle(targetUrl);

                            JTextArea bodyTextArea= new JTextArea(bodyData.get(targetUrl));
                            JScrollPane bodyScroll = new JScrollPane(bodyTextArea);
                            bodyScroll.setPreferredSize(new Dimension(450, 430));
                            // 设置一直显示滚动条
                            bodyScroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                            viewBodyFrame.add(bodyScroll);
                            viewBodyFrame.setVisible(true);
//
//
                        }
                    });
                    // 添加到右键菜单
                    rightMenu.add(browserMenItem);
                    rightMenu.add(copyMenItem);
                    rightMenu.add(viewMenItem);

                    // 右键显示菜单栏
                    if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                        //通过点击位置找到点击为表格中的行
                        int focusedRowIndex = table.rowAtPoint(e.getPoint());
                        if (focusedRowIndex == -1) {
                            return;
                        }
                        //将表格所选项设为当前右键点击的行
                        table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        targetUrl = (String) table.getValueAt(focusedRowIndex,0);
                        //弹出菜单
                        rightMenu.show(table, e.getX(), e.getY());}

                    // 双击默认浏览器打开
                    if (e.getClickCount() == 2){
                        int focusedRowIndex = table.rowAtPoint(e.getPoint());
                        if (focusedRowIndex == -1) {
                            return;
                        }
                        table.setRowSelectionInterval(focusedRowIndex, focusedRowIndex);
                        targetUrl = (String) table.getValueAt(focusedRowIndex,0);
                        try {
                            java.net.URI uri = java.net.URI.create(targetUrl);
                            // 获取当前系统桌面扩展
                            java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                            // 判断系统桌面是否支持要执行的功能
                            if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                                dp.browse(uri);
                                // 获取系统默认浏览器打开链接
                            }
                        } catch (java.lang.NullPointerException e1) {
                            // 此为uri为空时抛出异常
                            e1.printStackTrace();
                        } catch (java.io.IOException e1) {
                            // 此为无法获取系统默认浏览器
                            e1.printStackTrace();
                        }
                    }
                }
            });
        };



    public void httpData(ArrayList<String> str, final String cookiesBody, final String uaBody, final String headBody, final String methods, final String dataBody, final String enctypeBody){
        // 获取线程池数量
        if (threadBox.getSelectedItem() == "--请选择线程--"){
            pool = Executors.newFixedThreadPool(1);
        }else {
            pool = Executors.newFixedThreadPool(Integer.valueOf(threadBox.getSelectedItem().toString()));
        }
        // 是否跟随302跳转
        final boolean follow = followBox.isSelected();

            for (String s : str) {
                final String target = s;
                // SwingWorker线程
                SwingWorker worker = new SwingWorker<Vector,Void>() {
                    @Override
                    protected Vector doInBackground() throws Exception {
                        Vector<Object> list = new Vector<>();
                        ArrayList<String> responseData = new ArrayList<String>();
//                        System.out.println(responseData);
                        String responseTitle;
                        try {
//                            if (target.startsWith("http://") || target.startsWith("https://")) {
                            if (proxyPower == false){
                            responseData = Http.Response(target, cookiesBody, uaBody, headBody, methods, dataBody, enctypeBody,follow);
                            }else {
                                System.out.println(host);
                                System.out.println(port);
                                responseData = Http.Response(target, cookiesBody, uaBody, headBody, methods, dataBody, enctypeBody,follow,host,port);
                            }
                                responseTitle = Re.Title(responseData.get(1));
                                list.addElement(target);
                                list.addElement(responseData.get(0));
                                list.addElement(responseTitle);
                                list.addElement(responseData.get(2));
                                list.addElement(responseData.get(3));
//                                list.addElement(responseData.get(1));

                            bodyData.put(target,responseData.get(1));
                            if (responseData == null) {
                                synchronized (this) {
                                    System.out.println("异常");
                                    total +=1;
                                }
                            }

                            System.out.println("[1]" + Thread.currentThread().getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                            list.addElement(target);
//                            list.addElement("无法访问");
                            list.addElement("<html><span style=\"color: red;\">"+"无法访问"+"</span></html>");
                            list.addElement(e.getMessage());
                            bodyData.put(target,"");
                        }
                        return list;
                    }

                    @Override
                    protected void done() {
                        Vector<Object> list = new Vector<>();
                        try {
                            list = get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        dataModel.addRow(list);
                        doneCount += 1;
                        countLabel.setText(doneCount + "/" + total);
                        if (doneCount == total){
                            // 完成时开启run按钮
                            runButton.setEnabled(true);
                        }
                    }
                };
                //线程执行
                pool.execute(worker);

        }
    }

    public void exportTable(JTable table, File file) throws IOException {
        TableModel model = table.getModel();
        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"GBK"));
        for(int i=0; i < model.getColumnCount(); i++) {
            bWriter.write(model.getColumnName(i));
            bWriter.write(",");
        }
        bWriter.newLine();
        for(int i=0; i< model.getRowCount(); i++) {
            for(int j=0; j < model.getColumnCount(); j++) {
                //解决Java导出csv文件数据中包含逗号导致乱行的问题
                System.out.println(model.getValueAt(i,j));
                String value;
                if (model.getValueAt(i,j) != null){
                    value = model.getValueAt(i,j).toString();
                }else{
                    value = "";
                }
                if (value.contains(",")){
                    value = "\""+value+"\"";
                }
                bWriter.write(value);
                bWriter.write(",");
            }
            bWriter.newLine();
        }
        bWriter.close();
        System.out.println("write out to: " + file);
    }

    // 关于事件
    class about implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            JOptionPane.showMessageDialog(null, "WEB批量请求器 \nVersion 1.3\nAuthor：@xiaowei\nGithub: https://github.com/ScriptKid-Beta/WebBatchRequest\nCopyright 2021", "关于",JOptionPane.WARNING_MESSAGE);//弹框提示
        }
    }

    class proxy implements ActionListener{
        public void actionPerformed(ActionEvent e){
            final JDialog proxyFrame = new JDialog();
            proxyFrame.setBounds(795,350,335,118);
            proxyFrame.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));
            proxyFrame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

            proxyFrame.setTitle("代理设置");

            final JLabel hostJLabel = new JLabel();
            hostJLabel.setText("地址:");
            final JTextField hostJText = new JTextField(11);

            JLabel portJLabel = new JLabel();
            portJLabel.setText("端口:");
            final JTextField portJText = new JTextField(5);

            final JButton defineJButton = new JButton("确定");
            JButton cancelJButton = new JButton("取消");

            proxyFrame.add(hostJLabel);
            proxyFrame.add(hostJText);

            proxyFrame.add(portJLabel);
            proxyFrame.add(portJText);

            proxyFrame.add(defineJButton);
            proxyFrame.add(cancelJButton);
            // 确认按钮事件
            defineJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    host = hostJText.getText();
                    portString = portJText.getText();

                    if (!"".equals(host) && "".equals(portString)){
                        JOptionPane.showMessageDialog(defineJButton, "端口不能为空", "error",JOptionPane.WARNING_MESSAGE);
                    }
                    if (!"".equals(portString) && "".equals(host)){
                        JOptionPane.showMessageDialog(defineJButton, "代理服务器不能为空", "error",JOptionPane.WARNING_MESSAGE);
                    }
                    if ("".equals(host) && "".equals(portString)){
                        proxyPower = false;
                        proxyFrame.dispose();
                    }
                    if (!"".equals(host) && !"".equals(portString)){
                        proxyPower = true;
                        port = Integer.valueOf(portString);
                        proxyFrame.dispose();}

                }
            });
            hostJText.setText(host);
            portJText.setText(portString);

            // 取消按钮事件
            cancelJButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proxyFrame.dispose();
                }
            });
            proxyFrame.setVisible(true);
        }

    }


}

