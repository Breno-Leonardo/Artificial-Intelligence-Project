package trabIA;

import java.util.ArrayList;
 
public class Estado  {

	private int grade[][];
	
	
	private Movimento movimento;
	private ArrayList<Movimento> historicoMovimentos= new ArrayList<Movimento>();
	
	public Estado(int tamanho,int grade[][], Movimento movimento,ArrayList<Movimento> historicoMovimentos,int prioridade) {
		super();
		this.grade = new int[tamanho][tamanho];
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
			this.grade[i][j]=grade[i][j];
			}
		}
		this.historicoMovimentos=historicoMovimentos;
		this.movimento = movimento;
		
	}


	public int[][] getGrade() {
		return grade;
	}

	public void setGrade(int[][] grade) {
		this.grade = grade;
	}

	public Movimento getMovimento() {
		return movimento;
	}

	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
	}

	public ArrayList<Movimento> getHistoricoMovimentos() {
		return historicoMovimentos;
	}

	public void setHistoricoMovimentos(ArrayList<Movimento> historicoMovimentos) {
		this.historicoMovimentos = historicoMovimentos;
	}

	
	

	
	
}
