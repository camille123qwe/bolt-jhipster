import project from 'app/entities/app1/project/project.reducer';
import device from 'app/entities/app1/device/device.reducer';
import strategy from 'app/entities/app1/strategy/strategy.reducer';
import executionType from 'app/entities/app1/execution-type/execution-type.reducer';
import source from 'app/entities/app1/source/source.reducer';
import customer from 'app/entities/app1/customer/customer.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  project,
  device,
  strategy,
  executionType,
  source,
  customer,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
