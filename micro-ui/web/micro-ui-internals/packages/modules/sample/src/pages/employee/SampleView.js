import React from 'react'
import ViewEstimateComponent from '../../components/ViewEstimateComponent';
const View = () => {
  const ViewEstimate = Digit.ComponentRegistryService.getComponent("ViewEstimatePage");
  return (
    <ViewEstimate />
  )
}

export default View;