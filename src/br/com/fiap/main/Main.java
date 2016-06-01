package br.com.fiap.main;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class Main {

	public static void main(String[] args) {

		try {

			List<Status> tweets = new ArrayList<Status>();

			List<String> listaNomes = new ArrayList<String>();

			List<String> listaNomeSobrenome = new ArrayList<String>();

			int qtdRet = 0;

			int qtdFav = 0;

			DateTimeFormatter formataData = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate semana = LocalDate.now().minusWeeks(1);

			//Configuração do Token

			Twitter twitter = configurarToken();

			//hashtag escolhida para o trabalho

			Query query = new Query("#openjdk");

			query.setSince(formataData.format(semana));

			query.setUntil(formataData.format(semana.plusDays(1)));

			QueryResult queryResult = twitter.search(query);

			for(int i = 1; i <= 7; i ++ ) {

				while(queryResult.hasNext()) {

					query = queryResult.nextQuery();

					tweets.addAll(queryResult.getTweets());

					for(Status status : queryResult.getTweets()) {

						//Pegando o nomes do usuário

						listaNomes.add(status.getUser().getName());

						//Contanto os retweets

						qtdRet += status.getRetweetCount();

						//Contando as favoritações

						qtdFav += status.getFavoriteCount();

					}

					queryResult = twitter.search(query);

				}

				semana = semana.plusDays(1);

				query = new Query("#openjdk");

				query.setSince(formataData.format(semana));

				query.setUntil(formataData.format(semana.plusDays(1)));

				queryResult = twitter.search(query);

			}

			//Ordenar os tweets pelo nome do autor, e exibir o primeiro nome e o último nome.

			Collections.sort(listaNomes);

			for (String nome : listaNomes) {

				String[] split = nome.split(" ");

				if (split.length == 1) {

					String primeiroNome = split[0];

					listaNomeSobrenome.add(primeiroNome);

				} else {

					String primeiroNome = split[0];

					String ultimoNome = split[split.length - 1];

					listaNomeSobrenome.add(primeiroNome + " " + ultimoNome);

				}

			}

			//Ordenar os tweets por data

			Date dataMaior = tweets.get(0).getCreatedAt();

			Date dataMenor = tweets.get(0).getCreatedAt();

			for(int i= 0; i< tweets.size(); i ++ ) {

				if(dataMaior.before(tweets.get(i).getCreatedAt())) {

					//Data mais recente

					dataMaior = tweets.get(i).getCreatedAt();

				}else if(dataMenor.after(tweets.get(i).getCreatedAt())) {

					//Data menos recente

					dataMenor = tweets.get(i).getCreatedAt();

				}

			}

			//Mostrando os resultados na console

			imprimirConsole(tweets, qtdRet, qtdFav, listaNomeSobrenome, dataMenor, dataMaior);

			//Enviar Twitter ao Professor

			enviarTwitterProfessor("@michelpf", twitter, "Olá @michelpf!"

			+ "\nTag Utilizada: #openjdk"

			+ "\nÚltima Semana:"

			+ "\n Qtd de tweets: " + tweets.size()

			+ "\n Qtd de retweets: " + qtdRet

			+ "\n Qtd de favoritações: " + qtdFav);

			enviarTwitterProfessor("@michelpf", twitter, "Olá @michelpf!"

			+ "\nContinuação das informações do trabalho"

			+ "\n Data menos recente: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataMenor)

			+ "\n Data mais recente: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataMaior));

		}catch(TwitterException e) {

			System.out.println(e.getMessage());

		}

	}

	private static Twitter configurarToken() throws TwitterException {

		ConfigurationBuilder configurationBuilderbuilder = new ConfigurationBuilder();

		configurationBuilderbuilder.setOAuthConsumerKey("v6GRXCdhKCH2aKrfwqlSrKxHN");

		configurationBuilderbuilder.setOAuthConsumerSecret("mCrQEt64eR7Zh4As7ckSHPDox0WVXpmqwDj9LDJ7U3vIgHATCc");

		Configuration configuration = configurationBuilderbuilder.build();

		TwitterFactory factoryfactory = new TwitterFactory(configuration);

		Twitter twitter = factoryfactory.getInstance();

		AccessToken accessToken = loadAccessToken();

		twitter.setOAuthAccessToken(accessToken);

		return twitter;

	}

	private static AccessToken loadAccessToken() throws TwitterException {

		String token = "74519694-T4ZORRNMLSBdHeEDB0ACJjPHn4it2JCrFQEeH2X3p";

		String tokenSecret = "nBNwaDszujFF195tp1Hv8yb38CCliTRBA3Q3YMuXGjZ6I";

		return new AccessToken(token, tokenSecret);

	}

	public static Status enviarTwitterProfessor(String twitterID,

			Twitter twitter, String status) throws TwitterException {

		return twitter.updateStatus(status);

	}

	public static void imprimirConsole(List<Status> tweets, int qtdRet, int qtdFav, List<String> listaNomeSobrenome,

			Date dataMenor, Date dataMaior) {

		System.out.println("1 - Quantidade de tweets da última semana: " + tweets.size());
		System.out.println("\n2 - Quantidade de retweets da última semana: " + qtdRet);
		System.out.println("\n3 - Quantidade de favoritações da última semana: " + qtdFav);
		System.out.println("\n4 - Ordenar os tweets pelo nome do autor, e exibir o primeiro nome e o último nome: \n" + listaNomeSobrenome);
		System.out.println("\n5 - Data menos recente: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataMenor));
		System.out.println("Data mais recente: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataMaior));

	}
}
