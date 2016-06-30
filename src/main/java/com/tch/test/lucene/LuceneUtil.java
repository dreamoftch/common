package com.tch.test.lucene;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class LuceneUtil {
	
	/**
	 * '文件内容'的索引名词
	 */
	public static final String CONTENT_INDEX_NAME = "contents";
	/**
	 * '文件修改时间'的索引名词
	 */
	public static final String MODIFIED_INDEX_NAME = "modified";
	/**
	 * '文件路径'的索引名词
	 */
	public static final String PATH_INDEX_NAME = "path";

	public static void main(String[] args) throws Exception {
		String indexPath = "/media/tch/disk1/study/temp/lucene-index";
		index("/media/tch/disk1/study/mytigase", indexPath);
		search(indexPath, "handleRichMJPacket");
	}

	/**
	 * 建立索引
	 * @param filePath 被索引的目录
	 * @param indexPath 索引存放目录
	 * @param create
	 * @throws IOException
	 */
	public static void index(String filePath, String indexPath) throws IOException {
		System.out.println("开始在目录 '" + indexPath + "' 下面建立索引文件...");
		final Path path = Paths.get(filePath);
		Date start = new Date();
		//获取IndexWriter
		IndexWriter writer = getIndexWriter(indexPath);
		
		index4Folder(writer, path);
		
		// writer.forceMerge(1);
		writer.close();
		System.out.println("创建索引一共用了" + (new Date().getTime() - start.getTime())/1000 + " 秒");
	}
	
	/**
	 * 获取IndexWriter
	 * @param docDir
	 * @param indexPath
	 * @return
	 * @throws IOException
	 */
	public static IndexWriter getIndexWriter(String indexPath) throws IOException{
		return new IndexWriter(FSDirectory.open(Paths.get(indexPath)), getIndexWriterConfig());
	}
	
	/**
	 * 获取IndexWriterConfig
	 * @return
	 */
	public static IndexWriterConfig getIndexWriterConfig(){
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(getAnalyzer());
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		return indexWriterConfig;
	}

	/**
	 * 为指定目录的所有文件创建索引
	 * @param writer
	 * @param path
	 * @throws IOException
	 */
	public static void index4Folder(final IndexWriter writer, Path path) throws IOException {
		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						index4File(writer, file.toUri().getPath(), attrs.lastModifiedTime().toMillis());
					} catch (IOException ignore) {
						// don't index files that can't be read.
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			index4File(writer, path.toUri().getPath(), Files.getLastModifiedTime(path).toMillis());
		}
	}

	/**
	 * 为单个文件创建索引
	 * @param writer
	 * @param file
	 * @param lastModified
	 * @throws IOException
	 */
	public static void index4File(IndexWriter writer, String filePath, long lastModified) throws IOException {
		System.out.println("开始为文件 " + filePath + " 创建索引");
		Document document = new Document();
		//文件路径
		document.add(new StringField(PATH_INDEX_NAME, filePath, Field.Store.YES));
		//修改时间
		document.add(new LongPoint(MODIFIED_INDEX_NAME, lastModified));
		//文件内容
		document.add(new TextField(CONTENT_INDEX_NAME, getFileContent(filePath), Store.YES));
		if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
			//添加doc
			writer.addDocument(document);
		} else {
			//更新doc
			writer.updateDocument(new Term("path", filePath), document);
		}
	}
	
	public static String getFileContent(String filePath) throws IOException{
		StringBuilder builder = new StringBuilder("");
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				builder.append(line);
				builder.append("\r\n");
			}
		} finally {
			if(bufferedReader != null){
				bufferedReader.close();
			}
		}
		return builder.toString();
	} 

	/**
	 * 通过索引搜索字符串
	 * @param indexPath
	 * @param targetString
	 * @throws Exception
	 */
	public static void search(String indexPath, String targetString) throws Exception {
		//在索引的contents字段上面进行搜索
		String indexField = "contents";
		search(indexPath, targetString, indexField);
	}
	
	/**
	 * 通过索引搜索字符串
	 * @param indexPath 索引存放路径
	 * @param searchStr 要搜索的字符串
	 * @throws Exception
	 */
	public static void search(String indexPath, String searchStr, String indexField) throws Exception {
		//解析器
		QueryParser parser = new QueryParser(indexField, getAnalyzer());
		//Query
		Query query = parser.parse(searchStr);
		//索引reader
		IndexReader reader = getIndexReader(indexPath);
		//索引搜索器
		IndexSearcher searcher = new IndexSearcher(reader);
		doSearch(searcher, query);
		reader.close();
	}
	
	/**
	 * 根据搜索字符串和索引列名称获取Query
	 * @param searchStr
	 * @param indexField
	 * @return
	 * @throws ParseException
	 */
	public static Query getQuery(String searchStr, String indexField) throws ParseException{
		//解析器
		QueryParser parser = new QueryParser(indexField, getAnalyzer());
		return parser.parse(searchStr);
	}
	
	/**
	 * 获取IndexReader
	 * @param indexPath 索引存放路径
	 * @return
	 * @throws IOException
	 */
	public static IndexReader getIndexReader(String indexPath) throws IOException{
		return DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
	}
	
	/**
	 * 获取Analyzer
	 * @return
	 */
	public static Analyzer getAnalyzer(){
		return new StandardAnalyzer();
	}
	
	/**
	 * 执行搜索
	 * @param searcher
	 * @param query
	 * @throws IOException
	 */
	public static void doSearch(IndexSearcher searcher, Query query) throws IOException {
		TopDocs results = searcher.search(query, 50);
		ScoreDoc[] hits = results.scoreDocs;
		System.out.println("一共搜索到 " + results.totalHits + " 条结果， 下面展示 " + hits.length + "条结果");
		for (int i = 0; i < hits.length; i++) {
			//System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
			Document document = searcher.doc(hits[i].doc);
			System.out.println((i + 1) + ". 文件路径: " + document.get(PATH_INDEX_NAME));
			System.out.println((i + 1) + ". 文件内容: " + document.get(CONTENT_INDEX_NAME));
		}
	}
}