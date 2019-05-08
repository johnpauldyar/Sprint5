/**
 * 
 */
package software_masters.planner_networking;

import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lee.kendall
 * @author wesley murray
 */

public class Department implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2291616780795200489L;
	/**
	 * Object which stores the planFiles of a particular department based on year
	 */
	private ConcurrentHashMap<String, PlanFile> planFileMap;
	

	public Department()
	{
		planFileMap = new ConcurrentHashMap<String, PlanFile>();
	}

	/**
	 * Retrieves planFile from department based on year
	 * 
	 * @param year
	 * @return plan corresponding to the passed year
	 * @throws IllegalArgumentException
	 */
	public PlanFile getPlan(String year) throws IllegalArgumentException
	{
		if (planFileMap.containsKey(year))
		{
			return planFileMap.get(year);
		}
		throw new IllegalArgumentException("A plan with this year doesn't exist in this department");
	}

	/**
	 * Returns a collection of the planFiles of the department
	 * 
	 * @return Collection<PlanFile>
	 */
	public Collection<PlanFile> getPlans()
	{
		return planFileMap.values();

	}

	/**
	 * Adds new planFile to department hash with a corresponding year. Overwrites
	 * old planFile if a planFile with the passed year already exists.
	 * 
	 * @param year of planFile to be added
	 * @param plan
	 */
	public void addPlan(String year, PlanFile plan)
	{
		planFileMap.put(year, plan);
	}

	/**
	 * Removes planFile from department hash given a year
	 * 
	 * @param year of planFile to be removed
	 */
	public void removePlan(String year)
	{
		planFileMap.remove(year);
	}

	/**
	 * Checks if the plan exists within this department
	 * 
	 * @param year
	 * @return
	 */
	public boolean containsPlan(String year)
	{
		return planFileMap.containsKey(year);
	}

	/**
	 * @return the planFileMap
	 */
	public ConcurrentHashMap<String, PlanFile> getPlanFileMap()
	{
		return planFileMap;
	}

	public String getPlanFileMapString()
	{
		return planFileMap.toString();
	}

	/**
	 * @param planFileMap the planFileMap to set
	 */
	public void setPlanFileMap(ConcurrentHashMap<String, PlanFile> planFileMap)
	{
		this.planFileMap = planFileMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((planFileMap == null) ? 0 : planFileMap.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Department other = (Department) obj;
		if (planFileMap == null)
		{
			if (other.planFileMap != null)
			{
				return false;
			}
		} else if (!Department.<String, PlanFile>hashesEqual(planFileMap, other.planFileMap))
		{
			return false;
		}
		return true;
	}

	private static <K, V> boolean hashesEqual(ConcurrentHashMap<K, V> map1, ConcurrentHashMap<K, V> map2)
	{
		for (Enumeration<K> keyList = map1.keys(); keyList.hasMoreElements();)
		{
			K key = keyList.nextElement();
			if (!map1.containsKey(key))
			{
				return false;
			}
			if (!map2.containsKey(key))
			{
				return false;
			}
			if (!map1.get(key).equals(map2.get(key)))
			{
				return false;
			}
		}
		return true;
	}

}
