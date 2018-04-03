package cn.hmxin.readbookproject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class ReadBookActivity extends Activity {
	
	private TextView bookContent = null ;//��ʾ�ı������
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_book_layout);
		
		bookContent = (TextView)findViewById(R.id.book_content);//���ı����
		bookContent.setMovementMethod(ScrollingMovementMethod.getInstance());//���ı�������������
		
		String txtFilePath = getIntent().getStringExtra("txtFilePath");//��ȡ���������ı�·��
		if(txtFilePath != null){//����д�������
			int index = txtFilePath.lastIndexOf(File.separator);//������ȡ�ı������ֵĵ�һ������
			String name = txtFilePath.substring(index+1, txtFilePath.length());//��ȡ�ı�����
			setTitle(name);//���ñ���Ϊ�ı�����
			try {
				FileInputStream fr = new FileInputStream(txtFilePath);//�ļ������
				BufferedReader br = new BufferedReader(new InputStreamReader(fr, "utf-8"));//�����ȡ�ļ�����
				String line = "" ;//��¼ÿһ������
				String content = "" ;
				while((line = br.readLine()) != null){//���������һ������
					content += line + "\n" ;
				}
				bookContent.setText(content);;//׷����ʾ����
				br.close();//�ر��ļ������
				fr.close();//�رջ�����
			} catch (IOException e) {//�׳��쳣
				Toast.makeText(ReadBookActivity.this, "û�д��ļ���", Toast.LENGTH_SHORT).show();//��ʾ�쳣
				finish();//ֱ�ӹرս���
			}
		}
	}
}
