package com.concentrate.gpt.gptflow.service.tree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractStringTreeService<T extends Node> {

	private static Logger logger = LoggerFactory.getLogger(AbstractStringTreeService.class);

	private ArrayList<T> roots = new ArrayList<T>();
	private Map<String, T> nodeMap = new HashMap<String, T>();

	public abstract Collection<T> getAllTerminalNodes();

	public void initial() {
		try {
			Collection<T> allNodes = getAllTerminalNodes();
			initNodeMap(allNodes);
		} catch (Exception e) {
			logger.error("init dir tree error!", e);
		}
	}

	private void initNodeMap(Collection<T> allNodes) {

		if (allNodes != null) {
			Map<String, T> tmpMap = new HashMap<String, T>();
			roots = buildTreeFast(allNodes,tmpMap);
			nodeMap.clear();
			nodeMap = tmpMap;
		}
	}

	/**
	 * 非递归方式构建树
	 * @param nodes 节点列表
	 * @param allNodes 节点查找表返回用
	 * @return roots 所有跟节点
	 */
	public  static<T extends Node> ArrayList<T> buildTreeFast(Collection<T> nodes, Map<String,T> allNodes) {
		ArrayList<T> roots = new ArrayList<T>();
		if(nodes!=null&&nodes.size()>0){
			// 1 初始化查找表
			for(T node :nodes){
				allNodes.put(node.getId(),node);
			}
			// 2 找上下级关系
			for(T node :nodes){
				T parent = allNodes.get(node.getParentId());
				if(parent!=null){
					parent.getChildren().add(node);
					node.setParent(parent);
				}
			}
			// 找跟节点
			for(T node :nodes){
				T parent = allNodes.get(node.getParentId());
				if(parent==null){
					roots.add(node);
				}
			}

			// 初始化层级
			for(T node :roots){
				setLevel(node,1);
			}
		}
		return roots;
	}

	private static <T extends Node> void setLevel(T node, int i) {
		if(node!=null){
			node.setLevel(i);
			if(node.getChildren()!=null && node.getChildren().size()>0){
				for(Node child:node.getChildren()){
					setLevel(child,i+1);
				}
			}
		}
	}


	public T getNodeByNodeId(String id) {
		return nodeMap.get(id);
	}

	public Map<String,T> getNodeMap(){
		return nodeMap;
	}

	public Collection<T> getRoots() {
		return roots;
	}

}
