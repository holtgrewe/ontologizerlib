package ontologizer.benchmark;

import java.util.List;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.validators.PositiveInteger;


/**
 * The command line interface.
 *
 * @author Sebastian Bauer
 */
public class BenchmarkCLIConfig
{
	public static class ProperPositiveInteger implements IParameterValidator
	{
		@Override
		public void validate(String name, String value) throws ParameterException
		{
			int n = Integer.parseInt(value);
			if (n < 1)
				throw new ParameterException("Parameter " + name + " should be a proper positive integer(found " + value +")");
		}
	}

	public static class RateValueValidator implements IValueValidator<List<Double>>
	{
		@Override
		public void validate(String name, List<Double> value) throws ParameterException
		{
			for (double v : value)
			{
				if (v < 0.0 || v >= 1.0)
					throw new ParameterException("Parameter " + name + " should be between 0 and 1");
			}
		}
	}

	@Parameter(names={"--term-combinations-per-run"}, description="How many term combinations per should be drawn per run. A run consists of drawing identically sized sets of terms.", validateWith=ProperPositiveInteger.class)
	public int termCombinationsPerRun = 300;

	@Parameter(names={"--min-terms-per-combination"}, description="The minimum number of distinct terms per combination", validateWith=ProperPositiveInteger.class)
	public int minTerms = 1;

	@Parameter(names=("--max-terms-per-combination"), description="The maximum number of distinct terms per combination", validateWith=ProperPositiveInteger.class)
	public int maxTerms = 5;

	@Parameter(names={"--help"},description="Shows this help.",help=true)
	public boolean help;

	@Parameter(names={"--alpha"},variableArity=true, description="The false-positive rates to use when obsfuscating term combinations. Multiple values between 0 and 1 are accepted.", validateValueWith=RateValueValidator.class)
	public List<Double> alpha;

	@Parameter(names={"--beta"},variableArity=true, description="The false-negative rates to use when obsfuscating term combinations. Multiple values between 0 and 1 are accepted.", validateValueWith=RateValueValidator.class)
	public List<Double> beta;

	@Parameter(names={"--methods"}, variableArity=true, descriptionKey="methods")
	public List<String> methods;

	@Parameter(names={"-o", "--obo"}, description="The obo file that shall be used for running the benchmark. For instance, " +
				"\"http://www.geneontology.org/ontology/gene_ontology_edit.obo\"", arity=1, required=true)
	public String obo;

	@Parameter(names={"-a", "--association"}, description="Name of the file containing associations from items to terms. For instance, "+
				"\"http://cvsweb.geneontology.org/cgi-bin/cvsweb.cgi/go/gene-associations/gene_association.fb.gz?rev=HEAD\"", arity=1, required=true)
	public String assoc;

	@Parameter(names={"--proxy"}, description="Name of the proxy that shall be used for http connections.", arity=1)
	public String proxy;

	@Parameter(names={"--proxy-port"}, description="Port of the proxy that shall be used for http connections.", arity=1, validateWith=ProperPositiveInteger.class)
	public int proxyPort;

	@Parameter(names={"--output-dir"}, description="Folder where all the output is stored. Defaults to the current directory.", arity=1)
	public String outputDirectory;

	@Parameter(names={"--seed"}, description="Seed that should be used for random number generations. Specifying 0 means to generate one. " +
				"The seed will be stored in a file called \"seed\" in this case.", arity=1)
	public long seed = 0;

	@Parameter(names={"--num-processors"}, description="Specifies the number of processors that shall be used. Defaults to 0 which means to take all available.", arity=1, validateWith=PositiveInteger.class)
	public int numProcessors = 0;
}
