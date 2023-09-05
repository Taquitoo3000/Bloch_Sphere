import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import java.awt.EventQueue;
import java.awt.event.*;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import javax.swing.border.Border;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Main extends JPanel{
    float theta;
    float phi;

    public Main(float theta,float phi) {
        this.theta = theta;
        this.phi = phi;
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        setLayout(new BorderLayout());
        add(canvas3D);
        SimpleUniverse universo = new SimpleUniverse(canvas3D);

        BranchGroup escena = CrearGrafoEscena();
        escena.compile();
        universo.addBranchGraph(escena);
        universo.getViewingPlatform().setNominalViewingTransform();//vista predeterminada del universo
    }

    public BranchGroup CrearGrafoEscena(){
        BranchGroup objetoraiz = new BranchGroup();
        TransformGroup mouseGrupo = new TransformGroup();
        mouseGrupo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        mouseGrupo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objetoraiz.addChild(mouseGrupo);

        Appearance aparienciaroja = new Appearance();
        Material material = new Material();
        material.setAmbientColor(new Color3f(Color.DARK_GRAY));
        material.setDiffuseColor(new Color3f(Color.LIGHT_GRAY));
        material.setSpecularColor(new Color3f(Color.WHITE));
        material.setShininess(20.0f);
        aparienciaroja.setMaterial(material);
        Color3f grisclaro = new Color3f(Color.LIGHT_GRAY);
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(grisclaro);
        aparienciaroja.setColoringAttributes(ca);
        aparienciaroja.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.BLEND_SRC_ALPHA, 0.5f));


        LineArray eje = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
        eje.setCoordinate(0,new Point3f(0.4f,0f,0f));
        eje.setCoordinate(1,new Point3f(-0.4f,0f,0f));
        eje.setColor(0,new Color3f(Color.WHITE));
        eje.setColor(1,new Color3f(Color.WHITE));
        //objetoraiz.addChild(new Shape3D(eje));

        LineArray ejez = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
        ejez.setCoordinate(0,new Point3f(0f,0f,0.4f));
        ejez.setCoordinate(1,new Point3f(0f,0f,-0.4f));
        ejez.setColor(0,new Color3f(Color.WHITE));
        ejez.setColor(1,new Color3f(Color.WHITE));
        //objetoraiz.addChild(new Shape3D(ejez));

        LineArray ejey = new LineArray(2,LineArray.COORDINATES|LineArray.COLOR_3);
        ejey.setCoordinate(0,new Point3f(0f,0.4f,0f));
        ejey.setCoordinate(1,new Point3f(0f,-0.4f,0f));
        ejey.setColor(0,new Color3f(Color.WHITE));
        ejey.setColor(1,new Color3f(Color.WHITE));
        //objetoraiz.addChild(new Shape3D(ejey));


        Appearance ap_ejex = new Appearance();
        Color3f rojo = new Color3f(Color.GREEN);
        ColoringAttributes caex = new ColoringAttributes();
        caex.setColor(rojo);
        ap_ejex.setColoringAttributes(caex);

        Appearance ap_ejey = new Appearance();
        Color3f verde = new Color3f(Color.BLUE);
        ColoringAttributes caey = new ColoringAttributes();
        caey.setColor(verde);
        ap_ejey.setColoringAttributes(caey);

        Appearance ap_ejez = new Appearance();
        Color3f azul = new Color3f(Color.RED);
        ColoringAttributes caez = new ColoringAttributes();
        caez.setColor(azul);
        ap_ejez.setColoringAttributes(caez);

        //traslaciones de conos
        Transform3D trasx = new Transform3D();
        trasx.set(new Vector3f(0.41f,0f,0f));

        Transform3D trasy = new Transform3D();
        trasy.set(new Vector3f(0.0f,0.41f,0f));

        Transform3D trasz = new Transform3D();
        trasz.set(new Vector3f(0f,0f,0.41f));


        //rotaciones de conos(ejes)
        Transform3D rotejx = new Transform3D();
        rotejx.rotZ(Math.toRadians(270));
        Transform3D rotejz = new Transform3D();
        rotejz.rotX(Math.toRadians(90));
        //Agregamos las rotaciones a las traslaciones
        trasx.mul(rotejx);
        trasz.mul(rotejz);

        //Agregamos a los transform group la traslacion con la rotacion implementada
        TransformGroup tgex = new TransformGroup(trasx);
        TransformGroup tgey = new TransformGroup(trasy);
        TransformGroup tgez = new TransformGroup(trasz);

        //Esfera
        Sphere esfera = new Sphere(0.3f,aparienciaroja);
        //Conos(ejes)
        Cone c_ejex = new Cone(0.02f,0.05f,ap_ejex);
        Cone c_ejez = new Cone(0.02f,0.05f,ap_ejez);
        Cone c_ejey = new Cone(0.02f,0.05f,ap_ejey);
        //Agregamos a las traslaciones los conos de los ejes
        tgex.addChild(c_ejex);
        tgey.addChild(c_ejey);
        tgez.addChild(c_ejez);

        Color3f colorambiente = new Color3f(Color.LIGHT_GRAY);
        AmbientLight luzamb = new AmbientLight(colorambiente);
        luzamb.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));


        Color3f colorluz = new Color3f(Color.WHITE);
        Vector3f dirluz = new Vector3f(-1.0f,-1.0f,-1.0f);
        DirectionalLight luz = new DirectionalLight(colorluz,dirluz);
        luz.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0),100));

        //Creamos el vector que rotara
        Appearance apvector = new Appearance();
        Color3f negro = new Color3f(Color.YELLOW);
        ColoringAttributes cavector = new ColoringAttributes();
        cavector.setColor(negro);
        apvector.setColoringAttributes(cavector);

        Cylinder vector = new Cylinder(0.006f,0.26f,apvector);
        Transform3D trasvec = new Transform3D();
        trasvec.set(new Vector3f(0.f,0.13f,0f));
        Cone pvector = new Cone(0.03f,0.052f,apvector);
        Transform3D traspvec = new Transform3D();
        traspvec.set(new Vector3f(0f,0.27f,0f));

        Transform3D rotvec = new Transform3D();
        rotvec.rotZ(Math.toRadians(theta));
        //rotvec.rotY(Math.toRadians(phi));
        Transform3D rotvec2 = new Transform3D();
        rotvec2.rotY(Math.toRadians(phi+90f));
        rotvec2.mul(rotvec);
        Transform3D rotpvec = new Transform3D();
        rotpvec.rotZ(Math.toRadians(theta));
        //rotpvec.rotY(Math.toRadians(phi));
        Transform3D rotpvec2 = new Transform3D();
        rotpvec2.rotY(Math.toRadians(phi+90f));
        rotpvec2.mul(rotpvec);

        rotpvec2.mul(traspvec);
        rotvec2.mul(trasvec);


        TransformGroup tgvec = new TransformGroup(rotvec2);
        TransformGroup tgpvec = new TransformGroup(rotpvec2);


        tgvec.addChild(vector);
        tgpvec.addChild(pvector);


        objetoraiz.addChild(luz);
        objetoraiz.addChild(luzamb);
        mouseGrupo.addChild(esfera);
        mouseGrupo.addChild(new Shape3D(ejey));
        mouseGrupo.addChild(new Shape3D(ejez));
        mouseGrupo.addChild(new Shape3D(eje));
        mouseGrupo.addChild(tgez);
        mouseGrupo.addChild(tgey);
        mouseGrupo.addChild(tgex);
        mouseGrupo.addChild(tgvec);
        mouseGrupo.addChild(tgpvec);

        MouseRotate mr = new MouseRotate();
        mr.setTransformGroup(mouseGrupo);
        mr.setSchedulingBounds(new BoundingSphere(new Point3d(),1000f));
        objetoraiz.addChild(mr);

        return objetoraiz;
    }

    public static void main(String[] args) {
        System.setProperty("sun.awt.noerasebackground", "true");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Menu frame = new Menu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

class SoundReproducer implements ActionListener{
    AudioInputStream audioInputStream;
    Clip clip;

    public void ReproducirSonido(String nombreSonido){
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(nombreSonido).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Error al reproducir el sonido.");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        ReproducirSonido("SpaceLaserShot.wav");
    }
}

class Menu extends JFrame {
    JPanel contentPane;
    JComboBox<String> comboBox;
    float valortheta;
    float valorphi;
    Estado edo;
    Gate gate;
    SoundReproducer sonido = new SoundReproducer();
    public Menu() {

        setTitle("MENÚ");
        edo = new Estado();
        gate = new Gate();

        //(x,y,longitud,altura)
        setBounds(210, 200, 400, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        contentPane =new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        //Componentes

        //Etiquetas
        JLabel lblEstado = new JLabel("ESTADO");
        lblEstado.setBounds(250, 25, 100, 25);
        contentPane.add(lblEstado);

        JLabel lblOperador = new JLabel("Operador");
        lblOperador.setBounds(50, 250, 100, 25);
        contentPane.add(lblOperador);

        //label Estado inicial
        JLabel lbledoinicial = new JLabel("Estado Inicial");
        lbledoinicial.setBounds(250,125,125,25);
        contentPane.add(lbledoinicial);
        JLabel lbltheta = new JLabel("Θ =");
        lbltheta.setBounds(180,150,30,20);
        contentPane.add(lbltheta);
        JLabel lblphi = new JLabel("Φ =");
        lblphi.setBounds(180,180,30,20);
        contentPane.add(lblphi);
        JLabel lblestadoinicial = new JLabel("");
        lblestadoinicial.setBounds(100,220,300,20);
        contentPane.add(lblestadoinicial);
        JLabel lbledofinal = new JLabel("Estado Despues de Operar");
        lbledofinal.setBounds(100,500,300,20);
        contentPane.add(lbledofinal);
        JLabel lblestadofinal = new JLabel("");
        lblestadofinal.setBounds(100,525,300,20);
        contentPane.add(lblestadofinal);

        //label Matriz
        JLabel lblEqual = new JLabel("");
        lblEqual.setBounds(210, 276, 25, 25);
        contentPane.add(lblEqual);

        JLabel lblMatriz = new JLabel("");
        lblMatriz.setBounds(255, 255, 70, 25);
        contentPane.add(lblMatriz);
        JLabel lblMatriz2 = new JLabel("");
        lblMatriz2.setBounds(255, 295, 70, 25);
        contentPane.add(lblMatriz2);
        JLabel lblMatriz3 = new JLabel("");
        lblMatriz3.setBounds(230, 270, 25, 25);
        contentPane.add(lblMatriz3);
        JLabel lblMatriz4 = new JLabel("");
        lblMatriz4.setBounds(228, 270, 25, 25);
        contentPane.add(lblMatriz4);
        JLabel lblMatriz5 = new JLabel("");
        lblMatriz5.setBounds(222, 287, 25, 25);
        contentPane.add(lblMatriz5);

        //label Equation
        JLabel lblEquation = new JLabel("|ψ>");
        lblEquation.setBounds(30, 370, 355, 25);
        contentPane.add(lblEquation);
        JLabel lblEquation2 = new JLabel("");
        lblEquation2.setBounds(78, 364, 25, 25);
        contentPane.add(lblEquation2);
        JLabel lblEquation3 = new JLabel("");
        lblEquation3.setBounds(76, 364, 25, 25);
        contentPane.add(lblEquation3);
        JLabel lblEquation4 = new JLabel("");
        lblEquation4.setBounds(70, 381, 25, 25);
        contentPane.add(lblEquation4);

        //Imagenes
        JLabel imagen = new JLabel();
        ImageIcon icono = new ImageIcon("Bloch_sphere.png");
        imagen.setIcon(icono);
        imagen.setBounds(10,10,156,166);
        contentPane.add(imagen);

        JLabel ecuacion = new JLabel();
        ImageIcon iconoEcuacion = new ImageIcon("EquationsFondo.png");
        ecuacion.setIcon(iconoEcuacion);
        ecuacion.setBounds(180,50,218,72);
        contentPane.add(ecuacion);

        //Botones
        JButton boton_calcular = new JButton("CALCULAR");
        boton_calcular.setBounds(125, 450, 150, 25);
        contentPane.add(boton_calcular);
        JButton boton_save = new JButton("CAPTURAR");
        boton_save.setBounds(270, 160, 110, 25);
        contentPane.add(boton_save);
        JButton boton_help = new JButton("?");
        boton_help.setBounds(355, 635, 41, 25);
        boton_help.setForeground(Color.BLUE);
        contentPane.add(boton_help);
        JButton boton_inst = new JButton("Instructivo");
        boton_inst.setBounds(240, 635, 109, 25);
        contentPane.add(boton_inst);
        JButton boton_easter = new JButton("");
        boton_easter.setBounds(5, 645, 2, 2);
        contentPane.add(boton_easter);

        //ComboBox
        comboBox = new JComboBox<>();
        comboBox.setBounds(20, 275, 150, 25);
        contentPane.add(comboBox);

        comboBox.addItem("*Sin seleccionar*");
        comboBox.addItem("Identidad");
        comboBox.addItem("Hadamard");
        comboBox.addItem("Not");

        //textfield
        JTextField jtftheta = new JTextField();
        jtftheta.setBounds(210, 150, 50, 25);
        contentPane.add(jtftheta);
        JTextField jtfphi = new JTextField();
        jtfphi.setBounds(210, 180, 50, 25);
        contentPane.add(jtfphi);

        //Eventos
        comboBox.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                if(comboBox.getSelectedIndex()==0){
                    lblEqual.setText("");
                    lblMatriz.setText("");
                    lblMatriz2.setText("");
                    lblMatriz3.setText("");
                    lblMatriz4.setText("");
                    lblMatriz5.setText("");
                    lblEquation.setText("|ψ>");
                    lblEquation2.setText("");
                    lblEquation3.setText("");
                    lblEquation4.setText("");
                }
                else if(comboBox.getSelectedIndex()==1){
                    lblEqual.setText("=");
                    lblMatriz.setText("1       0");
                    lblMatriz2.setText("0       1");
                    lblMatriz3.setText("");
                    lblMatriz4.setText("");
                    lblMatriz5.setText("");
                    lblEquation.setText("Î|ψ>=|0><0|ψ>+|1><1|ψ>");
                    lblEquation2.setText("");
                    lblEquation3.setText("");
                    lblEquation4.setText("");
                }
                else if(comboBox.getSelectedIndex()==2){
                    lblEqual.setText("=");
                    lblMatriz.setText("1       1");
                    lblMatriz2.setText("1      -1");
                    lblMatriz3.setText("1");
                    lblMatriz4.setText("__");
                    lblMatriz5.setText("√2");
                    lblEquation.setText("Ĥ|ψ>=    (|0><0|ψ>+|1><0|ψ>+|0><1|ψ>-|1><1|ψ>)");
                    lblEquation2.setText("1");
                    lblEquation3.setText("__");
                    lblEquation4.setText("√2");
                }
                else if(comboBox.getSelectedIndex()==3){
                    lblEqual.setText("=");
                    lblMatriz.setText("0       1");
                    lblMatriz2.setText("1       0");
                    lblMatriz3.setText("");
                    lblMatriz4.setText("");
                    lblMatriz5.setText("");
                    lblEquation.setText("Û|ψ>|1><0|ψ>+|0><1|ψ>");
                    lblEquation2.setText("");
                    lblEquation3.setText("");
                    lblEquation4.setText("");
                }
            }
        });

        boton_save.addActionListener (new ActionListener(){
            public void actionPerformed (ActionEvent e){
                valortheta=Float.parseFloat(jtftheta.getText());
                valorphi=Float.parseFloat(jtfphi.getText());
                edo.setEdoAngulo(valortheta,valorphi);

                //Printear Estado String
                PrintEstado printearedo = new PrintEstado(edo);
                lblestadoinicial.setText(printearedo.getString());
            }
        });

        boton_calcular.addActionListener(new ActionListener(){
            public void actionPerformed (ActionEvent e){
                JFrame ventana = new JFrame("Esfera de Bloch");
                Main panel;
                edo.setEdoAngulo(edo.theta,edo.phi);
                PrintEstado printearedo = new PrintEstado(edo);
                lblestadoinicial.setText(printearedo.getString());
                if (comboBox.getSelectedIndex()==0){
                    PrintEstado printearedofinal = new PrintEstado(edo);
                    lblestadofinal.setText(printearedofinal.getString());
                    panel =new Main(edo.theta,edo.phi);
                }
                else if (comboBox.getSelectedIndex()==1){
                    edo.setArrayA(gate.aplicarI(edo.arrayA));
                    edo.setArrayBR(gate.aplicarI(edo.arrayBR));
                    edo.setArrayBI(gate.aplicarI(edo.arrayBI));
                    edo.setCoef(edo.juntarArrays(edo.arrayA, edo.arrayBR));
                    edo.calcularAngulos(edo.a, edo.b_r);
                    //reiniciar estado
                    edo.setEdoAngulo(edo.theta, edo.phi);
                    //Printear Estado String
                    PrintEstado printearedofinal = new PrintEstado(edo);
                    lblestadofinal.setText(printearedofinal.getString());
                    panel =new Main(edo.theta,edo.phi);
                }
                else if (comboBox.getSelectedIndex()==2){
                    edo.setArrayA(gate.aplicarH(edo.arrayA));
                    edo.setArrayBR(gate.aplicarH(edo.arrayBR));
                    edo.setArrayBI(gate.aplicarH(edo.arrayBI));
                    edo.setCoef(edo.juntarArrays(edo.arrayA, edo.arrayBR));
                    edo.calcularAngulos(edo.a, edo.b_r);
                    //reiniciar estado
                    edo.setEdoAngulo(edo.theta, edo.phi);
                    //Printear Estado String
                    PrintEstado printearedofinal = new PrintEstado(edo);
                    lblestadofinal.setText(printearedofinal.getString());
                    panel =new Main(edo.theta,edo.phi);
                }
                else if (comboBox.getSelectedIndex()==3){
                    edo.setArrayA(gate.aplicarU(edo.arrayA));
                    edo.setArrayBR(gate.aplicarU(edo.arrayBR));
                    edo.setArrayBI(gate.aplicarU(edo.arrayBI));
                    edo.setCoef(edo.juntarArrays(edo.arrayA, edo.arrayBR));
                    edo.calcularAngulos(edo.a, edo.b_r);
                    //reiniciar estado
                    edo.setEdoAngulo(edo.theta, edo.phi);
                    //Printear Estado String
                    PrintEstado printearedofinal = new PrintEstado(edo);
                    lblestadofinal.setText(printearedofinal.getString());
                    panel =new Main(edo.theta,edo.phi);
                }
                else {
                    panel = new Main(edo.theta, edo.phi);
                }

                ventana.add(panel);
                ventana.setSize(700, 700);
                ventana.setVisible(true);
                ventana.setLocationRelativeTo(null);
                sonido.ReproducirSonido("SpaceLaserShot.wav");
            }
        });

        boton_help.addActionListener (new ActionListener(){
            public void actionPerformed (ActionEvent e){
                sonido.ReproducirSonido("Qbit.wav");
                JOptionPane.showMessageDialog(null,"Un QBit, o Bit cuántico, es la unidad básica\n" +
                        "de procesamiento en la computación cuántica.\n\n" +
                        "Se comporta como un estado cuántico con sus\n" +
                        "debidas propiedades.\n\n" +
                        "La esfera de Bloch es una manera gráfica\n" +
                        "de visualizar un Qbit y lo que le pasa al\n" +
                        "aplicarle operadores.\n\n" +
                        "Los operadores se pueden representar\n" +
                        "matemáticamente como matrices, sin embargo\n" +
                        "computacionalmente se representan como\n" +
                        "compuertas lógicas.\n\n" +
                        "En este programa se puede visualizar tanto\n" +
                        "en notación de Dirac, como gráficamente\n" +
                        "lo que le pasa al QBit al pasar por una\n" +
                        "compuerta.", "Información que Qura", 3);
            }
        });

        boton_inst.addActionListener (new ActionListener(){
            public void actionPerformed (ActionEvent e){
                JFrame f = new JFrame("Instructivo");
                f.setBounds(700,400,425,220);
                JLabel imagen_ug = new JLabel();
                ImageIcon icono_ug = new ImageIcon("UGTO.png");
                imagen_ug.setIcon(icono_ug);
                imagen_ug.setBounds(0,0,83,98);
                JLabel lbl1 = new JLabel("1) Introduce los ángulos del estado");
                lbl1.setBounds(100, 20, 300, 25);
                JLabel lbl2 = new JLabel("2) Presiona capturar");
                lbl2.setBounds(100, 60, 300, 25);
                JLabel lbl3 = new JLabel("3) Selecciona el Operador a aplicar al estado");
                lbl3.setBounds(100, 100, 350, 25);
                JLabel lbl4 = new JLabel("4) Presiona Calcular");
                lbl4.setBounds(100, 140, 300, 25);
                f.add(lbl1);
                f.add(lbl2);
                f.add(lbl3);
                f.add(lbl4);
                f.add(imagen_ug);
                f.setVisible(true);
            }
        });

        boton_easter.addActionListener (new ActionListener(){
            public void actionPerformed (ActionEvent e){
                JFrame f = new JFrame("EASTER EGG");
                f.setBounds(1500,100,241,230);
                JLabel imagen_easter = new JLabel();
                ImageIcon icono_easter = new ImageIcon("Miau.jpeg");
                imagen_easter.setIcon(icono_easter);
                imagen_easter.setBounds(0,0,241,189);
                f.add(imagen_easter);
                f.setVisible(true);
                sonido.ReproducirSonido("Meow.wav");
            }
        });

    }
}

class PrintEstado{
    int bla;
    int bla2;
    int bla3;
    String alpha;
    String beta_r;
    String beta_i;

    public PrintEstado(Estado edo){
        alpha=Double.toString(edo.a);
        beta_r=Double.toString(edo.b_r);
        beta_i=Double.toString(edo.b_i);
        if(Math.abs(edo.a)<1e-10){
            alpha="0.0";
        }
        if(Math.abs(edo.b_r)<1e-10){
            beta_r="0.0";
        }
        if(Math.abs(edo.b_i)<1e-10){
            beta_i="0.0";
        }
        if (alpha.length()>3) {
            bla=4;
        }
        else if (alpha.length()<=3){
            bla=3;
        }
        if (beta_r.length()>3) {
            bla2=4;
        }
        else if (beta_r.length()<=3){
            bla2=3;
        }
        if (beta_i.length()>3) {
            bla3=4;
        }
        else if (beta_i.length()<=3){
            bla3=3;
        }
    }
    public String getString(){
        return "|ψ>="+alpha.substring(0,bla)+"|0>"+"+["+beta_r.substring(0,bla2)+"+i("+beta_i.substring(0,bla3)+")]|1>";
    }
}

class Estado{
    float theta;
    float phi;
    float a;
    float b_r;
    float b_i;
    float[] arrayA;
    float[] arrayBR;
    float[] arrayBI;
    float[][] qbit;
    //[n][m] n es el qubit, m es la posicion en n qubit
    public Estado(){
        qbit = new float[2][2];
        arrayA = new float[2];
        arrayBR = new float[2];
        arrayBI = new float[2];

        qbit[0][0]=1;
        qbit[0][1]=0;

        qbit[1][0]=0;
        qbit[1][1]=1;
    }

    public void setEdoAngulo(float x,float y) {
        theta=x;
        phi=y;
        a = (float) Math.cos(Math.toRadians(theta/2));
        b_r = (float) ( Math.cos(Math.toRadians(phi))*Math.sin(Math.toRadians(theta/2)));
        b_i = (float) (Math.sin(Math.toRadians(phi))*Math.sin(Math.toRadians(theta/2)));
        for(int i = 0; i<=1; i++){
            arrayA[i] = a*qbit[0][i];
        }
        for(int i = 0; i<=1; i++){
            arrayBR[i] = b_r*qbit[1][i];
        }
        for(int i=0;i<=1;i++){
            arrayBI[i] = b_i*qbit[1][i];
        }
    }

    public void setCoef(float[] x){
        a=x[0];
        b_r=x[1];
        System.out.println("setCoef:\t"+"a="+a+"\tb="+b_r+"\n");
    }

    public void setArrayA(float[] x){
        arrayA = x;
    }

    public void setArrayBR(float[] x){
        arrayBR = x;
    }

    public void setArrayBI(float[] x){
        arrayBI = x;
        b_i=arrayBI[1];
    }

    public float[] juntarArrays(float[] x, float[] y){
        float[] sol = new float[2];
        for(int i=0;i<=1;i++){
            sol[i]=x[i]+y[i];
            System.out.print(sol[i]+"="+"+"+x[i]+"+"+y[i]+"\t");
        }
        System.out.println();
        return sol;
    }

    public void calcularAngulos(float alpha, float beta_r){
        theta= 2*(float)Math.toDegrees(Math.acos(alpha));
        if(alpha==1){
            phi=0;
        }
        else {
            phi= (float) Math.toDegrees(Math.acos((double)beta_r/Math.sqrt(1-Math.pow(alpha,2))));
        }
        System.out.println("theta="+theta+"\tphi="+phi+"\ta="+a+"\tb_r"+b_r);
    }
}

class Gate{
    float[][] H;
    float[][] U;
    float[][] I;
    public Gate(){
        H= new float[2][2];
        U= new float[2][2];
        I= new float[2][2];

        I[0][0]=1;
        I[0][1]=0;  // 1  0
        I[1][0]=0;  // 0  1
        I[1][1]=1;

        H[0][0]=(float)(1/Math.sqrt(2));
        H[0][1]=H[0][0];                    // 1  1
        H[1][0]=H[0][0];                    // 1 -1
        H[1][1]=(float)(-1/Math.sqrt(2));

        U[0][0]=0;
        U[0][1]=1;  // 0  1
        U[1][0]=1;  // 1  0
        U[1][1]=0;
    }
    public float[] aplicarH(float[] a){
        float[] sol;
        sol = new float[2];
        sol[0]=0;
        sol[1]=0;
        for(int i=0;i<=1;i++){
            for(int j=0;j<=1;j++){
                sol[i] = sol[i] + H[j][i]*a[j];
            }
        }
        return sol;
    }

    public float[] aplicarU(float[] a){
        float[] sol;
        sol = new float[2];
        sol[0]=0;
        sol[1]=0;
        for(int i=0;i<=1;i++){
            for(int j=0;j<=1;j++){
                sol[i] = sol[i] + U[j][i]*a[j];
            }
        }
        System.out.println("aplicarU");
        for(int i=0;i<=1;i++){
            System.out.print(a[i]+"\t"+sol[i]+"\n");
        }
        System.out.println();
        return sol;
    }

    public float[] aplicarI(float[] a){
        return a;
    }
}
