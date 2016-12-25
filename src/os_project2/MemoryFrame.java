package os_project2;

import java.util.ArrayList;

public class MemoryFrame extends javax.swing.JFrame {

    String allocation_method;
    int deallocation;
    WholeMemory mem;
    Hole[] allholes = new Hole[20];
    Process p_allocated;
    int currentHole = 0;

    compareHoles[] cH = new compareHoles[100];
  

    compareHoles chosen;

    private void initHoles() {
        int stA = 0;
        for (int i = 0; i < 20; i++) {
            allholes[i] = new Hole(stA, 50);
            stA += 50;
        }
    }

    private void firstFit() {

        int counter = 0;
        int summation = 0;
        int moveNext = 0;
        boolean noSpace = false;
        int begin = 0;
        while ((summation < p_allocated.get_Size())) {

            if (((allholes[counter].occupied))) {
                counter++;
                summation = 0;
                moveNext = 0;
                noSpace = true;
                continue;
            }

            if (noSpace) {
                begin = counter;
                noSpace = false;
            }
            if (moveNext == 50) {
                counter++;
                moveNext = 0;
            }
            moveNext++;
            summation += 1;

        }

        if (noSpace) {

        } else {
            int z = begin;
            for (; z < counter; z++) {
                allholes[z].setoccupied();
                allholes[z].pList.add(p_allocated);
                allholes[z].free_space = 0;
            }

            if (moveNext > 0) {
                allholes[z].addProcess(p_allocated, moveNext);
            }
        }

        drawMemory();

    }

    private void bestFit() {

          
    for (int a=0 ; a<100;a++)
    {
        cH[a]=new compareHoles();
    }
        
        for (int i = 0; i < allholes.length; i++) {
            if (allholes[i].occupied) {
                currentHole++;
            } else if (allholes[i].pList.size()>0) {
                cH[++currentHole].size += 50 - allholes[i].getSizeOfList();
            } else {
                
                cH[currentHole].size += 50;
                cH[currentHole].addId(i);
            }
        }

        int cN = 0;
        int minSize = 2000;
        for (compareHoles cH1 : cH) {
            if(cH1.size==0)continue;
            
            if (cH1.size < minSize) {
                chosen = cH1;
                minSize = cH1.size;
            }

            cN++;
        }

        int temp = p_allocated.p_Size;
        
        for (Integer id : chosen.ids) {
            if(temp>0)
            {if (temp > 50) {
                if (allholes[id].free_space >= 50) {
                    allholes[id].setoccupied();
                    allholes[id].pList.add(p_allocated);
                    allholes[id].free_space = 0;
                    temp -= 50;
                } else {
                    allholes[id].setoccupied();
                    allholes[id].pList.add(p_allocated);
                    allholes[id].free_space = 0;
                    temp -= allholes[id].free_space;
                }
            } else {
                if (allholes[id].free_space >= 50) {
                    allholes[id].setoccupied();
                    allholes[id].pList.add(p_allocated);
                    allholes[id].free_space = 0;
                    temp =0;
                } else {
                    allholes[id].setoccupied();
                    allholes[id].pList.add(p_allocated);
                    allholes[id].free_space = 0;
                    int remain = allholes[id].free_space;
                    if (temp>remain)temp-=remain;else temp=0;
                }
            }
        }}
        
        chosen.ids=new ArrayList<>();
        chosen.size=0;
drawMemory();
    }

    class compareHoles {

        int size;
        ArrayList<Integer> ids;

        public compareHoles() {
            size = 0;
            ids = new ArrayList<>();
        }

        public void addId(int id) {
            ids.add(id);
        }
    }

    private void drawMemory() {

        output.setText(" Memory would be like : ");
        output.append("\n");

        int c = 0;

        for (int i = 0; i < allholes.length; i++) {
            output.append("\n");
            output.append(allholes[i].starting_address + "");
            output.append("\n");
            if (allholes[i].pList.size() > 0) {

                for (int z = 0; z < allholes[i].pList.size(); z++) {
                    
                    output.append("PROCESS " + allholes[i].pList.get(z).num);
                    output.append("\n");
                    
                     
                    if (allholes[i].pList.get(z).left_Size<50)
                    {
                         output.append(""+allholes[i].pList.get(z).p_Size);
                         allholes[i].pList.get(z).left_Size=0;
                         output.append("\n");
                     output.append( "Free");
                         output.append("\n");
                    }
                    else
                    {
                          allholes[i].pList.get(z).left_Size-=50;
                    }
                }

            } else {
                output.append("Free");
                output.append("\n");
            }

            output.append(allholes[i].end_address + "");
            output.append("\n");

        }

    }

    class Hole {

        int starting_address;
        int end_address;
        int hole_Size;
        int free_space;
        boolean occupied;
        ArrayList<Process> pList;

        public Hole(int starting_address, int size) {
            this.starting_address = starting_address;
            this.hole_Size = size;
            this.end_address = starting_address + size;
            this.free_space = hole_Size;
            this.occupied = false;
            pList = new ArrayList<>();
        }

        public void setoccupied() {
            occupied = true;
        }

        public void addProcess(Process p, int space) {
            pList.add(p);
            free_space -= space;
            if (free_space == 0) {
                this.occupied = true;
            }
        }

        public void reset() {
            occupied = false;
            free_space = hole_Size;
        }

        public int get_start() {
            return this.starting_address;
        }

        public int get_end() {
            return this.end_address;
        }

        public boolean containsProcess(int n) {
            for (Process pList1 : pList) {
                if (pList1.num == n) {
                    return true;
                }
            }
            return false;
        }

        public Process getProcess(int n) {
            for (Process pList1 : pList) {
                if (pList1.num == n) {
                    return pList1;
                }
            }
            return null;
        }

        public int getSizeOfList() {
            int sum = 0;
            for (Process pList1 : pList) {
                sum += pList1.p_Size;
            }
            return sum;
        }

    }

    class WholeMemory {

        Hole[] holes;

        public WholeMemory(Hole[] holes) {
            this.holes = holes;
        }

        public Hole[] get_holes() {
            return this.holes;
        }

        public void setHoles(Hole[] h) {
            this.holes = h;
        }

    }

    private void allocate() {
        if ("First Fit".equals(allocation_method)) {
            firstFit();
        } else {
            bestFit();
        }
    }

    private void deallocate() {

        for (Hole allhole : allholes) {
            if (allhole.containsProcess(deallocation)) {
                allhole.occupied = false;
                Process pTemp = allhole.getProcess(deallocation);
                allhole.free_space += pTemp.p_Size;
                allhole.pList.remove(pTemp);
            }
        }

        drawMemory();

    }

    class Process {

        int num;
        int starting_address;
        int end_address;
        int p_Size;
        int left_Size;

        public Process(int num, int size) {
            this.num = num;
            this.p_Size = size;
            this.left_Size=p_Size;

        }

        public void set_Start(int starting_address) {
            this.starting_address = starting_address;
        }

        public void set_end(int end_address) {
            this.end_address = this.starting_address + p_Size;
        }

        public int get_Start() {
            return this.starting_address;
        }

        public int get_end() {
            return this.end_address;
        }

        public int get_Size() {
            return this.p_Size;
        }

        public void set_size(int h) {
            this.p_Size = h;
        }

        public int get_number() {
            return this.num;
        }

        public void set_number(int n) {
            this.num = n;
        }
    }

    public MemoryFrame() {
        initComponents();
        setChoices();
        initHoles();
    }

    public final void setChoices() {
        method.add("First Fit");
        method.add("Best Fit");

    }

    public void getData() {

        allocation_method = method.getSelectedItem().toString();

        String pdata = processData.getText().toString();

        String[] values = pdata.split(",");
        String p_num = values[0];
        String p_size = values[1];

        int pn = Integer.parseInt(p_num);
        int pa = Integer.parseInt(p_size);
        p_allocated = new Process(pn, pa);

    }

    public void getDeallocatedProcessNumber() {

        String d_data = deallocatedProcess.getText().toString();
        deallocation = Integer.parseInt(d_data);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        processData = new javax.swing.JTextField();
        method = new java.awt.Choice();
        jLabel4 = new javax.swing.JLabel();
        deallocatedProcess = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        output = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        jLabel1.setText("Memory Allocation");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel2.setText("Allocation Method :");
        jLabel2.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel3.setText("Allocate Process :");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        jLabel4.setText("Deallocate Process :");

        jButton1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jButton1.setText("Allocate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jButton2.setText("Deallocate");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        output.setBackground(new java.awt.Color(204, 204, 204));
        output.setColumns(20);
        output.setFont(new java.awt.Font("Times New Roman", 3, 12)); // NOI18N
        output.setRows(5);
        jScrollPane1.setViewportView(output);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addComponent(method, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(processData)
                                    .addComponent(deallocatedProcess, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE))))
                        .addGap(111, 111, 111))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(318, 318, 318)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 204, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(390, 390, 390))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(method, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(processData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(deallocatedProcess, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        getData();
        allocate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        getDeallocatedProcessNumber();
        deallocate();
    }//GEN-LAST:event_jButton2ActionPerformed
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MemoryFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField deallocatedProcess;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private java.awt.Choice method;
    private javax.swing.JTextArea output;
    private javax.swing.JTextField processData;
    // End of variables declaration//GEN-END:variables
}
