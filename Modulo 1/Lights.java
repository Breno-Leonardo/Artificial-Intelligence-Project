package trabIA;

import java.util.ArrayList;
import java.util.Random;


public class Lights {
	private static int JA_APERTOU=1000000;
	private static int contadorMovimentosFeitos=0;
	private static int trocaDeEstados=0;
	private static ArrayList<Estado> estadosPrioridade= new ArrayList<Estado>();
	private static ArrayList<Estado> outrosEstados= new ArrayList<Estado>();
	public static void printGrade(int[][] grade, int tamanho) {
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				if (grade[i][j] == 1)
					System.out.print("■ ");
				else
					System.out.print("  ");
//				System.out.print(grade[i][j]);
			}
			System.out.println();
		}
	}

	public static void printGradeFA(double[][] gradeFA, int tamanho) {
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				System.out.print("|");
				System.out.print(gradeFA[i][j]);
				System.out.print("|");
			}
			System.out.println();
		}
	}

	public static boolean fazMovimento(int[][] grade, int tamanho, ArrayList<Movimento> historicoMovimentos) {
		
		double[][] gradeFA = new double[tamanho][tamanho];
		int acesos = 0;
		
		//calcula acesos no momento
		ArrayList<Integer> maioresDivisores = new ArrayList<Integer>();
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				if (grade[i][j] == 1)
					acesos++;
			}
		}
		
		// obtem os divisores em ordem
		int divisor = Math.min(5, tamanho);
		while (divisor != 0) {
			if (acesos % divisor == 0)
				maioresDivisores.add(divisor);
			divisor--;
		}

		//define a FA de cada quadrado
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				double parametro = 1;
				int FA = 0;
				if (grade[i][j] == 1)
					FA++;
				else
					FA--;
				if (i > 0) {
					if (grade[i - 1][j] == 1)
						FA++;
					else
						FA--;
				}
				if (i < tamanho - 1) {
					if (grade[i + 1][j] == 1)
						FA++;
					else
						FA--;
				}
				if (j > 0) {
					if (grade[i][j - 1] == 1)
						FA++;
					else
						FA--;
				}
				if (j < tamanho - 1) {
					if (grade[i][j + 1] == 1)
						FA++;
					else
						FA--;
				}
				
				//prioridade para divisores
				if (maioresDivisores.contains(FA)) {
					gradeFA[i][j] = parametro / FA;

				} else {
					if (FA < 0) {
						gradeFA[i][j] = 10000 / (-1 * FA);
					} else
						gradeFA[i][j] = parametro / FA + tamanho;

				}
				if (FA == 0)
					gradeFA[i][j] = 1000;
				

			}
		}
		
		// marcando os que ja foram apertados
		for (int i = 0; i < historicoMovimentos.size(); i++) {
			Movimento mov= historicoMovimentos.get(i);
			gradeFA[mov.i][mov.j]=JA_APERTOU;
		}
		
		//selecionando o min FA
		double min = gradeFA[0][0];
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				if (min > gradeFA[i][j]) {
					min = gradeFA[i][j];
				}
			}
		}
		
		//ja apertou todos os quadrados
		if(min==JA_APERTOU) {
			return false;
		}
		//printGradeFA(gradeFA, tamanho);
		
		//selecioando as acoes com FA igual a min
		ArrayList<Movimento> FAsIguais = new ArrayList<Movimento>();
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				if (gradeFA[i][j] == min)
					FAsIguais.add(new Movimento(i, j));

			}
		}
		
		
		//escolhe 1 dos melhores 
		Random random = new Random();
		int numMovimento = random.nextInt(FAsIguais.size());
		Movimento escolhido=FAsIguais.remove(numMovimento);
		//salva o resto como estados possiveis com boa prioridade
		for (Movimento movimento : FAsIguais) {
			ArrayList<Movimento> aux= new ArrayList<Movimento>();
			aux.addAll(historicoMovimentos);
			estadosPrioridade.add(new Estado(tamanho,grade,movimento,aux,0));
		}
		
		//salvando os estados sem prioridade
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				boolean add= true;
				for (Movimento movimento : FAsIguais) {
					if((movimento.i==i && movimento.j==j) || (movimento.i==escolhido.i && movimento.j==escolhido.j) ) {
						add=false;
					}
				}
				if(add ) {
					ArrayList<Movimento> aux= new ArrayList<Movimento>();
					aux.addAll(historicoMovimentos);
					outrosEstados.add(new Estado(tamanho,grade,new Movimento(i, j),aux,1));
				}
				

			}
		}
		historicoMovimentos.add(escolhido);
		
		contadorMovimentosFeitos++;
		int i = escolhido.i;
		int j = escolhido.j;
		grade[i][j] *= -1;
		if (i > 0)
			grade[i - 1][j] *= -1;
		if (i < tamanho - 1)
			grade[i + 1][j] *= -1;

		if (j > 0)
			grade[i][j - 1] *= -1;

		if (j < tamanho - 1)
			grade[i][j + 1] *= -1;

		
		
		//printGrade(grade, tamanho);
		return true;
	}
	
	public static boolean fazMovimentoEstado(int[][] grade,Movimento mov, int tamanho, ArrayList<Movimento> historicoMovimentos) {
		contadorMovimentosFeitos++;
		int i = mov.i;
		int j = mov.j;
		grade[i][j] *= -1;
		if (i > 0)
			grade[i - 1][j] *= -1;
		if (i < tamanho - 1)
			grade[i + 1][j] *= -1;

		if (j > 0)
			grade[i][j - 1] *= -1;

		if (j < tamanho - 1)
			grade[i][j + 1] *= -1;
		historicoMovimentos.add(mov);

		

		//printGrade(grade, tamanho);
		return true;
	}
	public static boolean fazMovimentoPrint(int[][] grade,Movimento mov, int tamanho) {
		int i = mov.i;
		int j = mov.j;
		grade[i][j] *= -1;
		if (i > 0)
			grade[i - 1][j] *= -1;
		if (i < tamanho - 1)
			grade[i + 1][j] *= -1;

		if (j > 0)
			grade[i][j - 1] *= -1;

		if (j < tamanho - 1)
			grade[i][j + 1] *= -1;
		
		printGrade(grade, tamanho);
		return true;
	}
	
	public static boolean estaResolvido(int[][] grade, int tamanho) {
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				if (grade[i][j] == 1)
					return false;
			}
		}
		return true;
	}
	public static void inicializando(int[][] grade, int tamanho) {
		for (int i = 0; i < tamanho; i++) {
			for (int j = 0; j < tamanho; j++) {
				grade[i][j] = 1;
			}
		}
		printGrade(grade, tamanho);
	}
	
	
	public static void main(String[] args) {
		int tamanho =5;
		int[][] grade = new int[tamanho][tamanho];
		ArrayList<Movimento> historicoMovimentos = new ArrayList<Movimento>();
		
		System.out.println("Grade Inicial:");
		inicializando(grade, tamanho);
		while (estaResolvido(grade, tamanho) != true ) {
			
			boolean saida=fazMovimento(grade, tamanho, historicoMovimentos);
			
				if(saida==false) {
					Estado estado;
					// tenta os melhores estados
					if(estadosPrioridade.size()>0)
					 estado= estadosPrioridade.remove(0);
					else
						estado= outrosEstados.remove(0);
					
					grade=estado.getGrade();
					trocaDeEstados++;
					historicoMovimentos=estado.getHistoricoMovimentos();
					fazMovimentoEstado(grade,estado.getMovimento(), tamanho, historicoMovimentos);

				}
			
			
		}
		System.out.println("ESTADOS:"+(estadosPrioridade.size()+outrosEstados.size()));
		System.out.println("Trocou de estados:"+trocaDeEstados);
		System.out.println("Fez " + contadorMovimentosFeitos + " movimentos");
		System.out.println("Caminho final faz "+historicoMovimentos.size()+ " movimentos");
		
		inicializando(grade, tamanho);
		for (int i = 0; i < historicoMovimentos.size(); i++) {
			System.out.println("Movimento "+i+": ("+(historicoMovimentos.get(i).i+1)+","+(historicoMovimentos.get(i).j+1)+")");
			fazMovimentoPrint(grade, historicoMovimentos.get(i), tamanho);
		}
		

	}
}
