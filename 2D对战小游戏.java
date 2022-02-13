package 代码部分;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
public class TestGamePanel extends JPanel implements KeyListener, ActionListener {
    //初始化人物坐标
    int p1X;
    int p1Y;
    int p2X;
    int p2Y;
    boolean isStart = false;    //游戏是否开始
    boolean p1isFail = false;     //游戏是否失败
    boolean p2isFail = false;
    String fx1;         //左：L， 右：R， 上：U， 下：D
    String fx2;
    Timer timer = new Timer(50,this);//定时器
    //积分
    int p1score = 0;
    int p2score = 0;
    //苹果
    int AppleX;
    int AppleY;
    //怪物
    int monster1X;
    int monster1Y;
    int monster2X;
    int monster2Y;
    int monster3X;
    int monster3Y;
    int monster4X;
    int monster4Y;
    int monster5X;
    int monster5Y;
    //随机积分
    Random random = new Random();
    public TestGamePanel() {
        init();
        this.setFocusable(true);
        this.addKeyListener(this);
        timer.start();
    }
    //初始化
    public void init() {
        p1X = 25;
        p1Y = 150;
        p2X = 700;
        p2Y = 550;
        fx1 = "L";
        fx2 = "R";
        monster1X = 25*random.nextInt(28);
        monster1Y = 100 + 25*random.nextInt(18);
        monster2X = 25*random.nextInt(28);
        monster2Y = 100 + 25*random.nextInt(18);
        monster3X = 25*random.nextInt(28);
        monster3Y = 100 + 25*random.nextInt(18);
        monster4X = 25*random.nextInt(28);
        monster4Y = 100 + 25*random.nextInt(18);
        monster5X = 25*random.nextInt(28);
        monster5Y = 100 + 25*random.nextInt(18);
        AppleX = 25*random.nextInt(28);
        AppleY = 100 + 25*random.nextInt(18);
        add(kaishi);
        add(chongkai);
        guize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TestGameRule();
            }
        });
    }
    //游戏功能按钮
    JButton kaishi = new JButton("开始");
    JButton chongkai = new JButton("重新开始");
    JButton guize = new JButton("游戏规则");
    //画板
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TestGameData.header.paintIcon(this,g,0,0);
        g.setColor(Color.CYAN);
        g.fillRect(0,100,780,520);
        //画人物
        TestGameData.p1player1.paintIcon(this,g,p1X,p1Y);
        TestGameData.p2player1.paintIcon(this,g,p2X,p2Y);
        //画得分
        g.setFont(new Font("Century Gothic",Font.BOLD,18));  //设置字体
        g.setColor(Color.RED);
        g.drawString("玩家1：" + p1score,20,20 );
        g.drawString("玩家2：" + p2score,680,20);
        //画苹果
        TestGameData.apple.paintIcon(this,g,AppleX,AppleY);
        //画静态怪物
        TestGameData.monster.paintIcon(this,g,monster1X,monster1Y);
        TestGameData.monster.paintIcon(this,g,monster2X,monster2Y);
        TestGameData.monster.paintIcon(this,g,monster3X,monster3Y);
        TestGameData.monster.paintIcon(this,g,monster4X,monster4Y);
        TestGameData.monster.paintIcon(this,g,monster5X,monster5Y);
        //游戏提示，是否开始
        if(!isStart) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Century Gothic",Font.BOLD,30));
            g.drawString("请点击开始游戏",300,300);
        }
        //游戏结束提示，是否重新开始
        if(p2isFail || p1score == 15) {
            g.setColor(Color.RED);
            g.setFont(new Font("Century Gothic",Font.BOLD,30));
            g.drawString("玩家一获胜，请点击重新开始游戏",200,300);
        }
        if(p1isFail || p2score == 15) {
            g.setColor(Color.RED);
            g.setFont(new Font("Century Gothic",Font.BOLD,30));
            g.drawString("玩家二获胜，请点击重新开始游戏",200,300);
        }
    }
    //键盘监听事件
    @Override
    public void keyPressed(KeyEvent e) {
        //控制人物走动
        //玩家1
        if(isStart == true && (p1isFail == false && p2isFail == false)) {
            if(e.getKeyCode() == KeyEvent.VK_D) {
                fx1 = "R";
                p1X += 25;
                if(p1X >= 750) {p1X = 750;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_A) {
                fx1 = "L";
                p1X -= 25;
                if(p1X <= 0) {p1X = 0;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_W) {
                fx1 = "U";
                p1Y -= 25;
                if(p1Y <= 100) {p1Y = 100;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_S) {
                fx1 = "D";
                p1Y += 25;
                if(p1Y >= 600) {p1Y = 600;}
            }
            //玩家2
            if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                fx2 = "R";
                p2X += 25;
                if(p2X >= 750) {p2X = 750;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                fx2 = "L";
                p2X -= 25;
                if(p2X <= 0) {p2X = 0;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_UP) {
                fx2 = "U";
                p2Y -= 25;
                if(p2Y <= 100) {p2Y = 100;}
            }
            else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                fx2 = "D";
                p2Y += 25;
                if(p2Y >= 600) {p2Y = 600;}
            }
        }
        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        kaishi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isStart = true;
            }
        });
        chongkai.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(p1isFail) { p1isFail = !p1isFail; init(); }
                if(p2isFail) { p2isFail = !p2isFail; init(); }
            }
        });
        add(kaishi);
        add(chongkai);
        add(guize);
        if(isStart == true && (p1isFail == false && p2isFail == false)) {
            //让人动起来
            if(fx1.equals("R")) {
                p1X += 25;
                if(p1X >= 750) { p1X = 750; }
            }
            if(fx1.equals("L")) {
                p1X -= 25;
                if(p1X <= 0) { p1X = 0; }
            }
            if(fx1.equals("U")) {
                p1Y -= 25;
                if(p1Y <= 100) { p1Y = 100; }
            }
            if(fx1.equals("D")) {
                p1Y += 25;
                if(p1Y >= 600) { p1Y = 600; }
            }
            if(fx2.equals("R")) {
                p2X += 25;
                if(p2X >= 750) { p2X = 750; }
            }
            if(fx2.equals("L")) {
                p2X -= 25;
                if(p2X <= 0) { p2X = 0; }
            }
            if(fx2.equals("U")) {
                p2Y -= 25;
                if(p2Y <= 100) { p2Y = 100; }
            }
            if(fx2.equals("D")) {
                p2Y += 25;
                if(p2Y >= 600) { p2Y = 600; }
            }
            //让怪物动起来
                //怪物1
                int i = random.nextInt(4) + 1;
                if(i == 1) {
                    monster1X += 5;
                    if(monster1X >= 750) {monster1X = 750;}
                }
                if(i == 2) {
                    monster1X -= 5;
                    if(monster1X <= 0) {monster1X = 0;}
                }
                if(i == 3) {
                    monster1Y += 5;
                    if(monster1Y >= 600) {monster1Y = 600;}
                }
                if(i == 4) {
                    monster1Y -= 5;
                    if(monster1Y <= 100) {monster1Y = 100;}
                }
            //怪物2
            int j = random.nextInt(4) + 1;
            if(j == 1) {
                monster2X += 5;
                if(monster2X >= 750) {monster2X = 750;}
            }
            if(j == 2) {
                monster2X -= 5;
                if(monster2X <= 0) {monster2X = 0;}
            }
            if(j == 3) {
                monster2Y += 5;
                if(monster2Y >= 600) {monster2Y = 600;}
            }
            if(j == 4) {
                monster2Y -= 5;
                if(monster2Y <= 100) {monster2Y = 100;}
            }
            //怪物3
            int k = random.nextInt(4) + 1;
            if(k == 1) {
                monster3X += 5;
                if(monster3X >= 750) {monster3X = 750;}
            }
            if(k == 2) {
                monster3X -= 5;
                if(monster3X <= 0) {monster3X = 0;}
            }
            if(k == 3) {
                monster3Y += 5;
                if(monster3Y >= 600) {monster3Y = 600;}
            }
            if(k == 4) {
                monster3Y -= 5;
                if(monster3Y <= 100) {monster3Y = 100;}
            }
            //怪物4
            int n= random.nextInt(4) + 1;
            if(n == 1) {
                monster4X += 5;
                if(monster4X >= 750) {monster4X = 750;}
            }
            if(n == 2) {
                monster4X -= 5;
                if(monster4X <= 0) {monster4X = 0;}
            }
            if(n == 3) {
                monster4Y += 5;
                if(monster4Y >= 600) {monster4Y = 600;}
            }
            if(n == 4) {
                monster4Y -= 5;
                if(monster4Y <= 100) {monster4Y = 100;}
            }
            //怪物5
            int m = random.nextInt(4) + 1;
            if(m == 1) {
                monster5X += 5;
                if(monster5X >= 750) {monster5X = 750;}
            }
            if(m == 2) {
                monster5X -= 5;
                if(monster5X <= 0) {monster5X = 0;}
            }
            if(m == 3) {
                monster5Y += 5;
                if(monster5Y >= 600) {monster5Y = 600;}
            }
            if(m == 4) {
                monster5Y -= 5;
                if(monster5Y <= 100) {monster5Y = 100;}
            }
            //如果有玩家吃到食物
            if(p1X == AppleX && p1Y == AppleY) {
                p1score++;
                AppleX = 25*random.nextInt(28);
                AppleY = 100 + 25*random.nextInt(18);
            } else if(p2X == AppleX && p2Y == AppleY) {
                p2score++;
                AppleX = 25*random.nextInt(28);
                AppleY = 100 + 25*random.nextInt(18);
            }
            //如果有玩家碰到怪物，判定死亡，游戏结束           后续有修改，暂用
            //怪物1死亡
            if(p1X >= monster1X -25 && p1X <= monster1X +25) {
                if(p1Y == monster1Y) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p1Y >= monster1Y -25 && p1Y <= monster1Y +25) {
                if(p1X == monster1X) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p2X >= monster1X -25 && p2X <= monster1X +25) {
                if(p2Y == monster1Y) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            if(p2Y >= monster1Y -25 && p2Y <= monster1Y +25) {
                if(p2X == monster1X) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            //怪物2死亡
            if(p1X >= monster2X -25 && p1X <= monster2X +25) {
                if(p1Y == monster2Y) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p1Y >= monster2Y -25 && p1Y <= monster2Y +25) {
                if(p1X == monster2X) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p2X >= monster2X -25 && p2X <= monster2X +25) {
                if(p2Y == monster2Y) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            if(p2Y >= monster2Y -25 && p2Y <= monster2Y +25) {
                if(p2X == monster2X) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            //怪物3死亡
            if(p1X >= monster3X -25 && p1X <= monster3X +25) {
                if(p1Y == monster3Y) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p1Y >= monster3Y -25 && p1Y <= monster3Y +25) {
                if(p1X == monster3X) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p2X >= monster3X -25 && p2X <= monster3X +25) {
                if(p2Y == monster3Y) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            if(p2Y >= monster3Y -25 && p2Y <= monster3Y +25) {
                if(p2X == monster3X) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            //怪物4死亡
            if(p1X >= monster4X -25 && p1X <= monster4X +25) {
                if(p1Y == monster4Y) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p1Y >= monster4Y -25 && p1Y <= monster4Y +25) {
                if(p1X == monster1X) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p2X >= monster4X -25 && p2X <= monster4X +25) {
                if(p2Y == monster4Y) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            if(p2Y >= monster4Y -25 && p2Y <= monster4Y +25) {
                if(p2X == monster4X) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            //怪物5死亡
            if(p1X >= monster5X -25 && p1X <= monster5X +25) {
                if(p1Y == monster5Y) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p1Y >= monster5Y -25 && p1Y <= monster5Y +25) {
                if(p1X == monster5X) { p1isFail = !p1isFail; p1score = p2score = 0;}
            }
            if(p2X >= monster5X -25 && p2X <= monster5X +25) {
                if(p2Y == monster5Y) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            if(p2Y >= monster5Y -25 && p2Y <= monster5Y+25) {
                if(p2X == monster5X) { p2isFail = !p2isFail; p1score = p2score = 0;}
            }
            //如果有玩家达到指定积分，判定获胜，游戏结束
            if(p1score == 15) { p2isFail = !p2isFail; }
            if(p2score == 15) { p1isFail = !p1isFail; }
            repaint();
        }
        timer.start();
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
