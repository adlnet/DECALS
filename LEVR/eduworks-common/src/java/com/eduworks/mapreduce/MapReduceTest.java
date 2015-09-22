package com.eduworks.mapreduce;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.List;

import org.junit.Test;

import com.eduworks.lang.EwList;
import com.eduworks.util.Tuple;

public class MapReduceTest {

	@Test
	public void test1() throws RemoteException, AlreadyBoundException {
		Tuple[] peers = { new Tuple<String, Short>("127.0.0.1", (short) 31231),
				new Tuple<String, Short>("127.0.0.1", (short) 31232),
				new Tuple<String, Short>("127.0.0.1", (short) 31233),
				new Tuple<String, Short>("127.0.0.1", (short) 31234) };
		MapReduceManager m1 = m2init(peers, 0);
		MapReduceManager m2 = m2init(peers, 1);
		MapReduceManager m3 = m2init(peers, 2);
		MapReduceManager m4 = m2init(peers, 3);

		for (int i = 0; i < 11; i++) {
			List<Object> map = m1.mapReduce(new Integer[] { 1, 2, 3, 4, 5, 6,
					7, 8, 9, 10 });
			System.out.println(map);
			if (i == 5)
				m2.cleanup();
			if (i == 4)
				m3.cleanup();
			if (i == 6)
				m4.cleanup();
			if (i == 8)
				m2 = m2init(peers, 1);
		}
		m1.cleanup();
	}

	private MapReduceManager m2init(Tuple[] datas, final int index)
			throws RemoteException, AlreadyBoundException {
		MapReduceManager m2 = new MapReduceManager("foo", (Short) datas[index]
				.getSecond(), new EwList<Tuple<String, Short>>(datas),
				new MapReduceListener() {

					public Object go(JobStatus key) throws RemoteException {
						if (index == 3)
							return null;
						if (index == 2)
							throw new RemoteException("FOO!");
						System.out.println("foo" + index);
						List<Integer> results = new EwList<Integer>();
						Integer[] input = (Integer[]) key.getObject();
						for (int i = 0; i < input.length; i++) {
							if (!key.isMine(i))
								continue;
							results.add(input[i] + 30);
						}
						return results;
					}
				});
		return m2;
	}

}
