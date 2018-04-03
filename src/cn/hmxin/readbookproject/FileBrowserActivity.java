package cn.hmxin.readbookproject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FileBrowserActivity extends Activity {
	
	private ListView fileList = null ;//��ʾ�ļ����б�
	private ArrayAdapter adapter = null ;//������	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_browser_layout);
		setTitle("ѡ�������ļ���");//���ı���
		fileList = (ListView)findViewById(R.id.file_list);//�����
		openFile(adapter, fileList);//���ļ�����������������ʾ���б���
		
		fileList.setOnItemClickListener(new OnItemClick());//�ļ��б���������������
		
	}
	
	/**
	 * �˷�������װ������
	 * @param adapter ArrayAdapter������
	 * @param list Ҫ��ʾ��ListView
	 */
	public void openFile(ArrayAdapter adapter, ListView list){
		List<SubFile> ndata = new ArrayList<SubFile>();//���ڴ���ļ��������ص���������
		String strPath = getIntent().getStringExtra("filename");//��ȡ�ϸ����洫����ֵ
		if(strPath == null){//û��������
			strPath = Environment.getExternalStorageDirectory().getPath();//��Ŀ¼
		}
		File pathFile = new File(strPath);//Ҫ��ʾ��Ŀ¼
		if(pathFile != null){//�����Ŀ¼
			File[] files = pathFile.listFiles();//��ȡĿ¼�µ������ļ������ļ�
			for(File file : files){//ȫ������
				if(new SubFile(file).toString()!=null){//�������ɸѡ�����ļ�
					ndata.add(new SubFile(file));//��ӵ�mdata������
				}
			}
			//����������
			adapter = new ArrayAdapter(FileBrowserActivity.this, android.R.layout.simple_list_item_1, ndata);
			list.setAdapter(adapter);//�����б������ȥ
		}else{//���ļ�����
			Toast.makeText(FileBrowserActivity.this , "�����ļ�Ϊ�գ�", Toast.LENGTH_SHORT).show();
		}
	}
	
	//�ļ��б�����ĵ�����������
	private class OnItemClick implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			SubFile subFile = (SubFile) parent.getAdapter().getItem(position);//��ȡ����洢��SubFile�࣬������Ի�ȡ����������·��
			String filename = subFile.getFile().getPath();//filename��ֵΪsubFile�����е�·��
			Intent intent = null;//����Intent�࣬������ת����
			if(subFile.getFile().isDirectory()){//���·��Ϊ�ļ���
				intent = new Intent(FileBrowserActivity.this, FileBrowserActivity.class);//������ת����Activity
				intent.putExtra("filename", filename);//����·��
			}else{//�ļ��Ļ�
				intent = new Intent(FileBrowserActivity.this, MainActivity.class);//��ת��������
				writeData(filename);//����writeData�������÷�������д������
			}
			startActivity(intent);//��ת����
			finish();//�������
		}
		
	}
	
	/**
	 * �÷�������д������
	 * @param str Ҫд���ֵ
	 */
	
	public void writeData(String str){
			File file = new File(MainActivity.MYPATH+File.separator+"bookPath.txt");//Ҫ�������ļ�
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));//��BufferedWriter��׷��д������
				bw.append(str);//׷������
				bw.newLine();//�µ�һ��
				bw.close();//�ر�BufferedWrite��
			} catch (IOException e) {//�׳��쳣
				e.printStackTrace();
			}
	}
	
}
