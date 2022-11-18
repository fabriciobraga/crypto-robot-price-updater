package com.fabricio.crypto.robot.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;

public class Util {

	public static final DateFormat formatter_BR = new SimpleDateFormat("dd/MM/yyyy");
	public static final DateFormat formatterImage = new SimpleDateFormat("yyyyMMdd_HHmm");
	public static final DateFormat formatter_codigo = new SimpleDateFormat("yyyyMMdd");
	
	public static final DateFormat formatterTicketAgora = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final DateFormat formatterDayAndHour = new SimpleDateFormat("dd/MM HH:mm:ss");
	public static final DateFormat formatterDateAndHour = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public static final DateFormat formatterMes = new SimpleDateFormat("MMMMM", new Locale("pt","BR"));
	
	public static final DateFormat formatterFecierj = new SimpleDateFormat("dd.MM.yyyy");
	
	public static final DateFormat formatterMarketAPI = new SimpleDateFormat("/yyyy/MM/dd/");

	public static String generateRandomPassword(int count) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = count;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		return buffer.toString();
	}
	
	public static Date zerarData(Date d) {
		if (d != null) {
			try {
				return Util.formatter_BR.parse(Util.formatter_BR.format(d));
			} catch (ParseException e) {
				// faz nada...
			}
		}
		return null;
	}
	
	public static int calculaDiferencaEmDias(Date antes, Date depois) {
		
		long diffInMillies = Math.abs(depois.getTime() - antes.getTime());
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    return (int)diff;
	}

	public static String formatString(String string, String mask) {
		try {
			javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(string);
		}catch(ParseException e) {
			System.out.println("ERRO EM PARSE EM" + string);
		}
		return string;
	}
	
	public static String formatBigDecimalToBRL(BigDecimal myNumber) {
		try {
			Locale ptBr = new Locale("pt", "BR");
			return NumberFormat.getCurrencyInstance(ptBr).format(myNumber);
		}catch(Exception e) {
			System.out.println("ERRO AO FORMATAR MOEDA: " + e.toString());
			return myNumber.toString();
		}
	}
	
	public static void main (String[] args) {
		
		String name1 = "fabriciO braga";
		String name2 = " fabricio braga";
		String name3 = " fabricio ";
		String name4 = " fabricio silva braGa";
		
		System.out.println("first (" + name1 + "): " + getFirstName(name1));
		System.out.println("last (" + name1 + "): " + getLastName(name1));
		System.out.println("---------");
		
		System.out.println("first (" + name2 + "): " + getFirstName(name2));
		System.out.println("last (" + name2 + "): " + getLastName(name2));
		System.out.println("---------");
		
		System.out.println("first (" + name3 + "): " + getFirstName(name3));
		System.out.println("last (" + name3 + "): " + getLastName(name3));
	}
	
	public static String generateRandomCode(int size) {
		
		int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(size)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();
	    
	    return generatedString;
	    
	}
	
	public static String getFirstName(String fullName) {
		if(StringUtils.hasLength(fullName)) {
			if(fullName.trim().indexOf(" ") != -1) {
				return capitalize((fullName.trim().toLowerCase()).substring(0, fullName.indexOf(" ")).trim());
			}else {
				return capitalize(fullName.trim().toLowerCase());
			}
		}
		return "";
	}
	
	public static String getLastName(String fullName) {
		if(StringUtils.hasLength(fullName)) {
			if(fullName.trim().indexOf(" ") != -1) {
				return capitalize(fullName.trim().toLowerCase().substring(fullName.indexOf(" ")).trim());
			}
		}
		return "";
	}
	
	public static String capitalize(String name) {
		
		if(StringUtils.hasLength(name) && name.length() > 1) {
		
			String firstLetter = name.toLowerCase().substring(0, 1);
		    String remainingLetters = name.toLowerCase().substring(1, name.length());
	
		    // change the first letter to uppercase
		    firstLetter = firstLetter.toUpperCase();
	
		    // join the two substrings
		   return firstLetter + remainingLetters;
		}
		return name;
	}
	
	public static BigDecimal calculaPorcentagem(String valorParcial, String valorTotal, int scale) {
		
		if(StringUtils.hasText(valorParcial) && StringUtils.hasText(valorTotal)) {
			
			BigDecimal parcial = new BigDecimal(valorParcial);
			BigDecimal total = new BigDecimal(valorTotal);
			BigDecimal result = parcial.divide(total, new MathContext(scale, RoundingMode.HALF_EVEN)).multiply(new BigDecimal("100"), new MathContext(scale, RoundingMode.HALF_EVEN));
			
			return result.setScale(scale, RoundingMode.HALF_UP);
		}
		return null;
	}
	
	public static long calculaDiferencaHoras(Date antes, Date depois) {
		try {
			if(antes != null && depois != null && antes.compareTo(depois) < 0) {
				long diff = depois.getTime() - antes.getTime();
				return (long) diff/(1000*60*60);
			}
			
		}catch(Exception e) {
			System.out.println("PROBLEMA AO CALCULAR A DIFERENCA EM HORAS!");
			System.out.println(e.toString());
		}
		return 0;
	}
	
	public static long calculaDiferencaSegundos(Date antes, Date depois) {
		try {
			if(antes.compareTo(depois) < 0) {
				long diff = depois.getTime() - antes.getTime();
				return (long) diff/(1000);
			}
			
		}catch(Exception e) {
			System.out.println("PROBLEMA AO CALCULAR A DIFERENCA EM SEGUNDOS!");
			System.out.println(e.toString());
		}
		return 0;
	}
	
	public static Date subtrairDias(Date d, int dias) {
		
		try {
			
			return DateUtils.addDays(d, -dias);
			
		}catch(Exception e) {
			System.out.println("problema ao subtrair " + dias + " da data: " + d);
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static Date subtrairHoras(Date d, int horas) {
		
		try {
			
			return DateUtils.addHours(d, -horas);
			
		}catch(Exception e) {
			System.out.println("problema ao subtrair " + horas + " da data: " + d);
			System.out.println(e.toString());
		}
		return null;
	}
}