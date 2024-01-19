import enterprise from 'app/entities/enterprise/enterprise.reducer';
import employee from 'app/entities/employee/employee.reducer';
import contract from 'app/entities/contract/contract.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  enterprise,
  employee,
  contract,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
