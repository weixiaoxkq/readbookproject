package cn.hmxin.readbookproject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ListView bookList = null ;//��ʾͼ��(txt)�б����
	private Button btnAdd = null ;//׷���鰴ť���
	private ArrayAdapter<String> adapter = null ;//������
	private List<String> data = null ; //���ڴ洢���ݣ����ص���������
	private List<String> pathData = null ;//��¼·��
	private static final int REMOVE_BOOK = Menu.FIRST;//�Ƴ��������Ĳ˵���ʶ
	//������Ҫʹ�õ���·��
	public static final String MYPATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "myBook";
	private int longClickPosition = 0 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bookList = (ListView)findViewById(R.id.book_list);//�����
		btnAdd = (Button)findViewById(R.id.btn_add);//�����
		
		createOrRead();//�������ȡ�ļ�������˳����ʾ����
		
		registerForContextMenu(bookList);//Ϊtxt�б����ע�������Ĳ˵�
		
		btnAdd.setOnClickListener(new OnClick());//���ͼ�鰴ť�ĵ�����������
		bookList.setOnItemClickListener(new OnItemClick());//ͼ���б���������������
		bookList.setOnItemLongClickListener(new OnItemLongClick());//ͼ���б���������������
		
	}
	
	//�ļ��Ĵ����Ͷ�ȡ
	public void createOrRead(){
		File dirFile = new File(MYPATH);//������ʵ����һ��File�࣬·��ΪMYPATH��·����ϸ�����棩
		if(!dirFile.exists()){//����ļ��в�����
			dirFile.mkdirs();//�����ļ���
		}
		File file = new File(dirFile,"bookPath.txt");//������ʵ����һ��File�࣬��·��ΪdirFile���ļ���ΪbookPath.txt
		if(!file.exists()){//����ļ�������
			try {
				file.createNewFile();//�����ļ�
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{//����ļ��Ѿ�����
			try {
				FileReader fr = new FileReader(file);//������ʵ����FileReader�࣬·��Ϊfile
				BufferedReader br = new BufferedReader(fr);//������ʵ����BufferedFile�࣬�����ȡ����
				data = new ArrayList<String>();//ʵ����List�࣬����������ݣ���������������
				pathData = new ArrayList<String>();//��¼·��
				String line = "" ;//���ڴ洢��ȡ��������
				
				while((line = br.readLine()) != null){//���������һ������
					int sub = line.lastIndexOf(File.separator);//��ȡ�ļ���ʹ��
					String strName = line.substring(sub+1, line.length());//��ȡ���ļ���
					data.add(strName);//����ļ���
					pathData.add(line);//��������ļ�·��
				}
				br.close();//�ر�BufferedReader��
				fr.close();//�ر�FileReader��
				//����������������
				adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
				bookList.setAdapter(adapter);//����������
			} catch (FileNotFoundException e) {//�׳��쳣
				e.printStackTrace();
			} catch (IOException e) {//�׳��쳣
				e.printStackTrace();
			}
		}
	}
	
	//��������ͼ���б��ѡ����¼�
	private class OnItemClick implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(MainActivity.this, ReadBookActivity.class);//������תActivityʹ�ã��˴���ת����ȡͼ�����
			intent.putExtra("txtFilePath", pathData.get(position));//����Ҫ�򿪵�txt�ļ�·��
			startActivity(intent);//��ת����
		}
		
	}
	//��������ͼ���б��ѡ����¼�
	private class OnItemLongClick implements AdapterView.OnItemLongClickListener{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			longClickPosition = position;//��¼�������б��ĸ�����
			return false;
		}
	}
	
	//�����������ͼ�鰴ť�ĵ����¼�
	private class OnClick implements View.OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);//��ת���ļ����ѡ�����
			startActivity(intent);//��ת����
		}
		
	}
	
	//���������Ĳ˵�
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(0, REMOVE_BOOK, 0, "�Ƴ�����");//��Ӳ˵�����
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getItemId() == REMOVE_BOOK){//�Ƴ�ͼ�鴦��
			removeBook();
		}
		return super.onContextItemSelected(item);
	}
	
	//�Ƴ�ͼ�鴦��
	public void removeBook(){
		try {
			data.remove(longClickPosition);//�Ƴ�����
			pathData.remove(longClickPosition);//�Ƴ�����
			FileWriter fw = new FileWriter(new File(MYPATH + File.separator + "bookPath.txt"));//���ö�ȡ�ļ�
			BufferedWriter bw = new BufferedWriter(fw);//��BufferedWriter��д������
			for(String str : pathData){//����pathData����
				bw.write(str);//д������
				bw.newLine();//�µ�һ��
			}
			bw.close();
			//����������������
			adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
			bookList.setAdapter(adapter);//����������
		} catch (IOException e) {//�׳��쳣
			e.printStackTrace();
		}
	}

}
